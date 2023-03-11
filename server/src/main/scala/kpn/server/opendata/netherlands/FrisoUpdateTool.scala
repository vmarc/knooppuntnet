package kpn.server.opendata.netherlands

import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.charset.Charset

object FrisoUpdateTool {
  private val log = Log(classOf[FrisoUpdateTool])

  def main(args: Array[String]): Unit = {

    val exit: Int = try {
      FrisoUpdateToolOptions.parse(args) match {
        case Some(options) =>
          Mongo.executeIn(options.databaseName) { database =>
            val overpassQueryExecutor = {
              if (options.remote) {
                new OverpassQueryExecutorRemoteImpl()
              }
              else {
                new OverpassQueryExecutorImpl()
              }
            }
            new FrisoUpdateTool(database, overpassQueryExecutor).update()
          }
          log.info("Done")
          0

        case None =>
          // arguments are bad, error message will have been displayed
          -1
      }
    }
    catch {
      case e: Throwable =>
        log.error(e.getMessage, e)
        -1
    }

    System.exit(exit)
  }

  class FrisoUpdateTool(database: Database, overpassQueryExecutor: OverpassQueryExecutor) {

    def update(): Unit = {
      //      val query = QueryNode(1181850468)
      //      // val query = QueryNetherlandsNodes() [cleanup if no longer needed!]
      //      val nodesXml = overpassQueryExecutor.executeQuery(None, query)
      //      FileUtils.writeStringToFile(new File("/kpn/friso/osm.xml"), nodesXml, Charset.forName("UTF-8"))

      updateFile("Renamed_ext.geojson", "rename.geojson")
      updateFile("Minor rename_ext.geojson", "minor-rename.geojson")
      updateFile("Removed_osm.geojson", "removed.geojson")
      updateFile("Added_ext.geojson", "added.geojson")
      updateFile("No change_ext.geojson", "no-change.geojson")
      updateFile("Moved short distance_ext.geojson", "moved-short-distance.geojson")
      updateFile("invalid_nodes_ext.geojson", "other.geojson")
    }
  }

  private def updateFile(source: String, target: String): Unit = {
    val sourceFileName = "/home/marc/wrk/soft/osm-knooppunten-0.3.0/results/" + source
    val sourceFile = new File(sourceFileName)
    val orignalJson = FileUtils.readFileToString(sourceFile, Charset.forName("UTF-8"))
    val prefix = """{"type": "FeatureCollection","features":"""
    val suffix = "}"
    val json = prefix + orignalJson + suffix
    val targetFileName = "/kpn/tiles-history/routedatabank/" + target
    val targetFile = new File(targetFileName)
    FileUtils.writeStringToFile(targetFile, json, Charset.forName("UTF-8"))
  }
}
