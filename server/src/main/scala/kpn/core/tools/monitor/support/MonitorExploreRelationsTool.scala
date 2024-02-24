package kpn.core.tools.monitor.support

import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl

import scala.xml.XML

object MonitorExploreRelationsTool {
  def main(args: Array[String]): Unit = {
    val overpassQueryExecutor = new OverpassQueryExecutorImpl()
    val tool = new MonitorExploreRelationsTool(overpassQueryExecutor)
    tool.explore()
  }
}

case class RouteCount(
  typeTagValue: String,
  routeTagValue: String,
  nodeNetworkCount: Int,
  noneNodeNetworkCount: Int
)

class MonitorExploreRelationsTool(overpassQueryExecutor: OverpassQueryExecutor) {
  def explore(): Unit = {
    val counts = routeCounts()
    println(s"|route|type|node_network|other|total|")
    println("|--|--|--:|--:|--:|")
    counts.foreach { count =>
      val count1 = count.nodeNetworkCount
      val count2 = count.noneNodeNetworkCount
      val total = count1 + count2
      println(s"|${count.routeTagValue}|${count.typeTagValue}|$count1|$count2|$total|")
    }
    val nodeNetworkCount = counts.map(_.nodeNetworkCount).sum
    val noneNodeNetworkCount = counts.map(_.noneNodeNetworkCount).sum
    val grandTotal = nodeNetworkCount + noneNodeNetworkCount
    println(s"\nnode network routes: $nodeNetworkCount")
    println(s"\nnon node network routes: $noneNodeNetworkCount")
    println(s"\ntotal routes: $grandTotal")
  }

  private def routeCounts(): Seq[RouteCount] = {
    Seq(
      "bicycle",
      "foot",
      "walking",
      "hiking",
      "mtb",
      "inline_skates",
      "motorboat",
      "canoe",
    ).flatMap { routeTagValue =>
      Seq(
        routeCount("route", routeTagValue),
        routeCount("superroute", routeTagValue),
      )
    }
  }

  private def routeCount(typeTagValue: String, routeTagValue: String): RouteCount = {
    val routeTags = s"['type'='$typeTagValue']['route'='$routeTagValue']"
    val nodeNetworkCount = {
      val query = s"relation$routeTags['network:type'='node_network'];out ids;"
      queryRouteCount(typeTagValue, routeTagValue, query)
    }
    val noneNodeNetworkCount = {
      val query = s"relation$routeTags['network:type'!='node_network'];out ids;"
      queryRouteCount(typeTagValue, routeTagValue, query)
    }
    RouteCount(
      typeTagValue,
      routeTagValue,
      nodeNetworkCount,
      noneNodeNetworkCount
    )
  }

  private def queryRouteCount(typeTagValue: String, routeTagValue: String, query: String): Int = {
    val xmlString = overpassQueryExecutor.execute(query)
    val xml = XML.loadString(xmlString)
    val ids = (xml.head \ "relation").map(r => (r \ "@id").text)
    ids.size
  }
}
