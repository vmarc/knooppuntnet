package kpn.core.tools.support

import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelation
import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.charset.Charset

object DownloadRelationTool {

  private val overpassUrl = "http://server-1:9005/api/overpass"
  private val routes = Seq(
    "route 1" -> 1432559,
  )

  def main(args: Array[String]): Unit = {
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl(overpassUrl)
    new DownloadRelationTool(overpassQueryExecutor).download()
    println("done")
  }
}

class DownloadRelationTool(overpassQueryExecutor: OverpassQueryExecutor) {

  def download(): Unit = {
    DownloadRelationTool.routes.foreach { case (name, relationId) =>
      println(s"downloading $name")
      val xmlString = overpassQueryExecutor.executeQuery(None, QueryRelation(relationId))
      val filename = new File(s"/Users/marc/tmp/xml/$relationId.xml")
      FileUtils.writeStringToFile(filename, xmlString, Charset.forName("UTF-8"))
    }
  }
}
