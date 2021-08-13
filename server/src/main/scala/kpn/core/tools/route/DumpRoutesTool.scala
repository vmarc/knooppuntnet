package kpn.core.tools.route

import kpn.api.custom.Timestamp
import kpn.core.loadOld.Parser
import kpn.core.mongo.Database
import kpn.core.mongo.util.Mongo
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.QueryRelation
import kpn.core.util.Log
import kpn.server.repository.RouteRepositoryImpl
import org.apache.commons.io.FileUtils

import java.io.File
import scala.xml.XML

object DumpRoutesTool {

  def main(args: Array[String]): Unit = {

    if (args.length < 2) {
      println("Usage: DumpRoutesTool host analysisDatabaseName")
      System.exit(-1)
    }
    val host = args(0)
    val analysisDatabaseName = args(1)
    val executor = new OverpassQueryExecutorImpl()
    Mongo.executeIn(analysisDatabaseName) { database =>
      new DumpRoutesTool(database, executor).run()
    }
  }
}

class DumpRoutesTool(database: Database, overpassQueryExecutor: OverpassQueryExecutor) {

  private val log = Log(classOf[DumpRoutesTool])
  private val routeRepository = new RouteRepositoryImpl(database)

  def run(): Unit = {

    log.info("Reading route ids")
    val routeIds = routeRepository.allRouteIds()

    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      if ((index + 1) % 100 == 0) {
        log.info(s"${index + 1}/${routeIds.size}")
      }
      val xmlString = readRouteXml(routeId)
      val xml = XML.load(xmlString)
      val rawData = new Parser().parse(xml)
      if (rawData.relations.nonEmpty) {
        FileUtils.writeStringToFile(routeFile(routeId), xmlString, "UTF-8")
      }
    }

    log.info("Done")
  }

  private def readRouteXml(routeId: Long): String = {
    val query = QueryRelation(routeId)
    overpassQueryExecutor.executeQuery(Some(Timestamp(2021, 4, 14, 17, 0, 25)), query)
  }

  private def routeFile(routeId: Long): File = {
    val idString = routeId.toString
    val subDirName = idString.substring(idString.length - 3)
    val subDir = new File("/kpn/routes", subDirName)
    subDir.mkdirs()
    val file = new File(subDir, s"$routeId.xml")
    file
  }
}
