package kpn.server.opendata.netherlands

import kpn.server.opendata.common.OpenDataTileBuilder

import java.io.FileInputStream

object OpenDataNetherlandsTileBuilderTool {
  def main(args: Array[String]): Unit = {
    new OpenDataNetherlandsTileBuilderTool().build()
  }
}

class OpenDataNetherlandsTileBuilderTool {

  def build(): Unit = {
    val nodes = readNodes().map(_.toOpenDataNode)
    val routes = readRoutes().map(_.toOpenDataRoute)
    new OpenDataTileBuilder().build(nodes, routes, "routedatabank/hiking")
  }

  private def readNodes(): Seq[RoutedatabankNode] = {
    val filename = "/kpn/opendata/netherlands/Wandelknooppunten (wgs84).json"
    val inputStream = new FileInputStream(filename)
    new RoutedatabankNodeReader().read(inputStream)
  }

  private def readRoutes(): Seq[RoutedatabankRoute] = {
    val filename = "/kpn/opendata/netherlands/Wandelnetwerken (wgs84).json"
    val inputStream = new FileInputStream(filename)
    new RoutedatabankRouteReader().read(inputStream)
  }
}
