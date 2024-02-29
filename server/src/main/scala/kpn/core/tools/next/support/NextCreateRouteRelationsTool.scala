package kpn.core.tools.next.support

import kpn.core.data.Data
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.tools.next.database.NextDatabase
import kpn.core.tools.next.database.NextDatabaseImpl
import kpn.core.tools.next.domain.NextRouteRelation
import kpn.core.util.Log
import kpn.database.util.Mongo.codecRegistry
import kpn.server.monitor.route.update.RelationTopLevelDataBuilder
import org.apache.commons.io.FileUtils
import org.mongodb.scala.MongoClient

import java.io.File
import scala.xml.XML

object NextCreateRouteRelationsTool {
  def main(args: Array[String]): Unit = {
    val client = MongoClient("mongodb://localhost:27017")
    try {
      val mongoDatabase = client.getDatabase("kpn-next").withCodecRegistry(codecRegistry)
      val database = new NextDatabaseImpl(mongoDatabase)
      val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
      val tool = new NextCreateRouteRelationsTool(database, overpassQueryExecutor)
      tool.createMonitorRelations()
    } finally {
      client.close()
    }
  }
}

class NextCreateRouteRelationsTool(database: NextDatabase, overpassQueryExecutor: OverpassQueryExecutor) {

  private val log = Log(classOf[NextCreateRouteRelationsTool])
  private val batchSize = 100

  def createMonitorRelations(): Unit = {
    val routeIds = collectRouteIds()
    log.info(s"collected ${routeIds.size} route ids")
    routeIds.sliding(batchSize, batchSize).zipWithIndex.foreach { case (batchRouteIds, index) =>
      log.info(s"${index * batchSize}/${routeIds.size}")
      createMonitorRelations(batchRouteIds)
    }
  }

  private def collectRouteIds(): Seq[Long] = {
    val allRouteIds = FileUtils.readFileToString(new File("/kpn/next/route-ids.txt"), "UTF-8").split("\n").map(_.toLong)
    val loadedRouteIds = database.routeRelations.stringIds().map(_.toLong)
    (allRouteIds.toSet -- loadedRouteIds.toSet).toSeq.sorted
  }

  private def createMonitorRelations(routeRelationIds: Seq[Long]): Unit = {
    val data = monitorRelationData(routeRelationIds)
    routeRelationIds.foreach { routeRelationId =>
      data.relations.get(routeRelationId) match {
        case Some(relation) =>
          database.routeRelations.save(NextRouteRelation(routeRelationId, relation))

        case None =>
          log.error(s"could note read routeId $routeRelationId")
          None
      }
    }
  }

  private def monitorRelationData(relationIds: Seq[Long]): Data = {
    val relations = relationIds.map(id => s"relation($id);").mkString
    val query = s"($relations);(._;>;);out meta;"
    val xmlString = overpassQueryExecutor.execute(query)
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    new RelationTopLevelDataBuilder(rawData, relationIds).data
  }
}
