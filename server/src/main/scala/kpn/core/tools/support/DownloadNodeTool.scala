package kpn.core.tools.support

import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryNode
import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.charset.Charset

object DownloadNodeTool {

  private val overpassUrl = "https://overpass-api.de/api/interpreter"
  private val nodes = Seq(
    "node 1" -> 5535092255L,
  )

  def main(args: Array[String]): Unit = {
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl(overpassUrl)
    new DownloadNodeTool(overpassQueryExecutor).download()
    println("done")
  }
}

class DownloadNodeTool(overpassQueryExecutor: OverpassQueryExecutor) {

  def download(): Unit = {
    DownloadNodeTool.nodes.foreach { case (name, nodeId) =>
      println(s"downloading $name")
      val xmlString = overpassQueryExecutor.executeQuery(None, QueryNode(nodeId))
      println(xmlString)
      val filename = new File(s"/Users/marc/tmp/xml/$nodeId.xml")
      FileUtils.writeStringToFile(filename, xmlString, Charset.forName("UTF-8"))
    }
  }
}
