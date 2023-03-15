package kpn.server.opendata.flanders

import kpn.server.opendata.common.OpenDataTileBuilder

import java.io.FileInputStream
import scala.xml.InputSource
import scala.xml.XML

object FlandersTileBuilderTool {
  def main(args: Array[String]): Unit = {
    new FlandersTileBuilderTool().build()
  }
}

class FlandersTileBuilderTool {

  def build(): Unit = {
    val nodes = readNodes().map(_.toOpenDataNode)
    val routes = readRoutes().map(_.toOpenDataRoute)
    new OpenDataTileBuilder().build(nodes, routes, "toerisme-vlaanderen/hiking")
  }

  private def readNodes(): Seq[FlandersNode] = {
    val filename = "/kpn/opendata/flanders/knoop_wandel.xml"
    val stream = new FileInputStream(filename)
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    new FlandersNodeParser().parse(xml)
  }

  private def readRoutes(): Seq[FlandersRoute] = {
    val filename = "/kpn/opendata/flanders/traject_wandel.xml"
    val stream = new FileInputStream(filename)
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    new FlandersRouteParser().parse(xml)
  }
}
