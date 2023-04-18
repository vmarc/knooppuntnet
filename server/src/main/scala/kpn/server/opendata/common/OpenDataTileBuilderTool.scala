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
import scala.xml.Elem
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
    // buildNetherlands()
    log.info("Done")
  }

  private def buildFlanders(): Unit = {
    buildFlandersHiking()
    buildFlandersCycling()
  }

  private def buildFlandersHiking(): Unit = {
    Log.context("Flanders") {
      val nodes = readFlandersHikingNodes().map(_.toOpenDataNode)
      val routes = readFlandersHikingRoutes().map(_.toOpenDataRoute)
      new OpenDataTileBuilder().build(nodes, routes, "opendata/flanders/hiking")
    }
  }


  private def readFlandersHikingNodes(): Seq[FlandersNode] = {
    log.info("Read hiking nodes")
    val filename = "/kpn/opendata/flanders/knoop_wandel.xml"
    new FlandersNodeParser().parse(toXml(filename), "knoop_wandel")
  }

  private def readFlandersHikingRoutes(): Seq[FlandersRoute] = {
    log.info("Read hiking routes")
    val filename = "/kpn/opendata/flanders/traject_wandel.xml"
    new FlandersRouteParser().parse(toXml(filename), "traject_wandel")
  }

  private def buildFlandersCycling(): Unit = {
    Log.context("Flanders") {
      val nodes = readFlandersCyclingNodes().map(_.toOpenDataNode)
      val routes = readFlandersCyclingRoutes().map(_.toOpenDataRoute)
      new OpenDataTileBuilder().build(nodes, routes, "opendata/flanders/cycling")
    }
  }

  private def readFlandersCyclingNodes(): Seq[FlandersNode] = {
    log.info("Read cycling nodes")
    val filename = "/kpn/opendata/flanders/knoop_fiets.xml"
    new FlandersNodeParser().parse(toXml(filename), "knoop_fiets")
  }

  private def readFlandersCyclingRoutes(): Seq[FlandersRoute] = {
    log.info("Read cycling routes")
    val filename = "/kpn/opendata/flanders/traject_fiets.xml"
    new FlandersRouteParser().parse(toXml(filename), "traject_fiets")
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

  private def toXml(filename: String): Elem = {
    val stream = new FileInputStream(filename)
    val inputSource = new InputSource(stream)
    XML.load(inputSource)
  }
}
