package kpn.server.opendata.common

import kpn.core.util.Log
import kpn.server.opendata.flanders.FlandersNode
import kpn.server.opendata.flanders.FlandersNodeParser
import kpn.server.opendata.flanders.FlandersRoute
import kpn.server.opendata.flanders.FlandersRouteParser
import kpn.server.opendata.netherlands.RoutedatabankNode
import kpn.server.opendata.netherlands.RoutedatabankNodeReader
import kpn.server.opendata.netherlands.RoutedatabankRoute
import kpn.server.opendata.netherlands.RoutedatabankRouteReader

import java.io.FileInputStream
import scala.xml.InputSource
import scala.xml.XML

object OpenDataTileBuilderTool {
  def main(args: Array[String]): Unit = {
    new OpenDataTileBuilderTool().build()
  }
}

class OpenDataTileBuilderTool {

  private val log = Log(classOf[OpenDataTileBuilderTool])

  def build(): Unit = {
    buildFlanders()
    buildNetherlands()
    log.info("Done")
  }

  private def buildFlanders(): Unit = {
    Log.context("Flanders") {
      val nodes = readFlandersNodes().map(_.toOpenDataNode)
      val routes = readFlandersRoutes().map(_.toOpenDataRoute)
      new OpenDataTileBuilder().build(nodes, routes, "opendata/flanders/hiking")
    }
  }

  private def readFlandersNodes(): Seq[FlandersNode] = {
    log.info("Read nodes")
    val filename = "/kpn/opendata/flanders/knoop_wandel.xml"
    val stream = new FileInputStream(filename)
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    new FlandersNodeParser().parse(xml)
  }

  private def readFlandersRoutes(): Seq[FlandersRoute] = {
    log.info("Read routes")
    val filename = "/kpn/opendata/flanders/traject_wandel.xml"
    val stream = new FileInputStream(filename)
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    new FlandersRouteParser().parse(xml)
  }

  private def buildNetherlands(): Unit = {
    Log.context("Netherlands") {
      val nodes = readNetherlandsNodes().map(_.toOpenDataNode)
      val routes = readNetherlandsRoutes().map(_.toOpenDataRoute)
      new OpenDataTileBuilder().build(nodes, routes, "opendata/netherlands/hiking")
    }
  }

  private def readNetherlandsNodes(): Seq[RoutedatabankNode] = {
    log.info("Read nodes")
    val filename = "/kpn/opendata/netherlands/Wandelknooppunten (wgs84).json"
    val inputStream = new FileInputStream(filename)
    new RoutedatabankNodeReader().read(inputStream)
  }

  private def readNetherlandsRoutes(): Seq[RoutedatabankRoute] = {
    log.info("Read routes")
    val filename = "/kpn/opendata/netherlands/Wandelnetwerken (wgs84).json"
    val inputStream = new FileInputStream(filename)
    new RoutedatabankRouteReader().read(inputStream)
  }
}
