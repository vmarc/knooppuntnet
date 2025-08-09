package kpn.core.tools.next.support

import com.mongodb.client.MongoClients
import kpn.api.custom.Timestamp
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.doc.RouteRelation
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelationStructure
import kpn.core.tools.config.Dirs
import kpn.core.tools.next.database.NextDatabase
import kpn.core.tools.next.database.NextDatabaseImpl
import kpn.core.tools.next.domain.NextRouteRelation
import kpn.core.util.Log
import kpn.database.util.Mongo.codecRegistry
import kpn.server.monitor.route.update.RelationTopLevelDataBuilder
import org.apache.commons.io.FileUtils

import java.io.File
import scala.xml.XML

object NextCreateRouteRelationsTool {
  def main(args: Array[String]): Unit = {
    val client = MongoClients.create("mongodb://localhost:27017")
    try {
      val mongoDatabase = client.getDatabase("kpn-next").withCodecRegistry(codecRegistry)
      val database = new NextDatabaseImpl(mongoDatabase)
      val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
      val tool = new NextCreateRouteRelationsTool(database, overpassQueryExecutor)
      tool.createRouteRelations()
    } finally {
      client.close()
    }
  }
}

class NextCreateRouteRelationsTool(database: NextDatabase, overpassQueryExecutor: OverpassQueryExecutor) {

  private val log = Log(classOf[NextCreateRouteRelationsTool])
  private val batchSize = 100

  def createRouteRelations(): Unit = {
    val routeIds = collectRouteIds()
    val routeIdsSize = routeIds.size
    log.info(s"collected $routeIdsSize route ids")
    val batches = routeIds.sliding(batchSize, batchSize)

    batches.zipWithIndex.foreach { case (batchRouteIds, index) =>
      log.info(s"${index * batchSize}/$routeIdsSize")
      createRouteRelationBatch(batchRouteIds)
    }
    /*
        val tasks = batches.map { batchRouteIds =>
          new Callable[String]() {
            def call(): String = {
              createRouteRelationBatch(batchRouteIds)
              "OK"
            }
          }
        }
        val executor = Executors.newFixedThreadPool(1)
        try {
          val futures = tasks.map { task =>
            executor.submit(task)
          }
          var todo = futures.size
          while (todo > 0) {
            Thread.sleep(5000)
            todo = futures.filterNot(_.isDone).size * batchSize
            log.info(s"$todo/${routeIds.size}")
          }
        }
        finally {
          executor.shutdown()
        }
    */
  }

  private def collectRouteIds(): Seq[Long] = {
    val file = new File(Dirs.root, "next/all-route-ids.txt")
    val allRouteIds = FileUtils.readFileToString(file, "UTF-8").split("\n").map(_.toLong)
    val loadedRouteIds = database.routeRelations.ids()
    (allRouteIds.toSet -- loadedRouteIds.toSet).toSeq.sorted
  }

  private def createRouteRelationBatch(routeRelationIds: Seq[Long]): Unit = {
    val data = queryTopLevelRelations(routeRelationIds)
    routeRelationIds.foreach { routeRelationId =>
      data.relations.get(routeRelationId) match {
        case Some(relation) =>
          val structure = if (relation.relationIdMembers.nonEmpty) {
            queryRelationStructure(routeRelationId)
          }
          else {
            None
          }
          database.routeRelations.save(
            NextRouteRelation(
              routeRelationId,
              relation,
              structure
            )
          )

        case None =>
          log.error(s"could note read routeId $routeRelationId")
          None
      }
    }
  }

  private def queryTopLevelRelations(relationIds: Seq[Long]): Data = {
    val relations = relationIds.map(id => s"relation($id);").mkString
    val meta = s"""[date:"${Timestamp.analysisStart.iso}"][timeout:1500][maxsize:24000000000]"""
    val query = s"$meta;($relations);(._;>;);out meta;"
    val xmlString = overpassQueryExecutor.execute(query)
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    new RelationTopLevelDataBuilder(rawData, relationIds).data
  }

  private def queryRelationStructure(relationId: Long): Option[RouteRelation] = {
    val xmlString = overpassQueryExecutor.executeQuery(Some(Timestamp.analysisStart), QueryRelationStructure(relationId))
    val filteredXmlString = xmlString.linesIterator.filter { line =>
      !(line.contains("<node id") || line.contains("<way id") || line.contains("<member type=\"node\"") || line.contains("<member type=\"way\""))
    }.mkString("\n")
    val xml = XML.loadString(filteredXmlString)
    val rawData = new Parser().parse(xml.head)
    val data = new DataBuilder(rawData).data
    data.relations.get(relationId).map { relation =>
      RouteRelation.from(relation, None)
    }
  }
}
