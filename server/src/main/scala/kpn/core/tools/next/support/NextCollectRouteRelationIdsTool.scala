package kpn.core.tools.next.support

import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.tools.config.Dirs
import org.apache.commons.io.FileUtils

import java.io.File
import scala.xml.XML

object NextCollectRouteRelationIdsTool {
  def main(args: Array[String]): Unit = {
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    val tool = new NextCollectRouteRelationIdsTool(overpassQueryExecutor)
    tool.collect()
  }
}

case class RouteInfo(
  typeTagValue: String,
  routeTagValue: String,
  nodeNetworkRouteIds: Seq[Long],
  noneNodeNetworkRouteIds: Seq[Long]
) {
  def routeIds: Seq[Long] = nodeNetworkRouteIds ++ noneNodeNetworkRouteIds
}

class NextCollectRouteRelationIdsTool(overpassQueryExecutor: OverpassQueryExecutor) {
  def collect(): Unit = {
    val routeInfos = queryRouteInfos()
    report(routeInfos)
    saveRouteIds(routeInfos)
  }

  private def report(routeInfos: Seq[RouteInfo]): Unit = {
    println("")
    println(s"|route|type|node_network|other|total|")
    println("|--|--|--:|--:|--:|")
    routeInfos.foreach { count =>
      val count1 = count.nodeNetworkRouteIds.size
      val count2 = count.noneNodeNetworkRouteIds.size
      val total = count1 + count2
      println(s"|${count.routeTagValue}|${count.typeTagValue}|$count1|$count2|$total|")
    }
    val nodeNetworkCount = routeInfos.map(_.nodeNetworkRouteIds.size).sum
    val noneNodeNetworkCount = routeInfos.map(_.noneNodeNetworkRouteIds.size).sum
    val grandTotal = nodeNetworkCount + noneNodeNetworkCount
    println(s"\nnode network routes: $nodeNetworkCount")
    println(s"\nnon node network routes: $noneNodeNetworkCount")
    println(s"\ntotal routes: $grandTotal")
  }

  private def saveRouteIds(routeInfos: Seq[RouteInfo]): Unit = {
    val file = new File(Dirs.root, "next/route-ids.txt")
    val routeIds = routeInfos.flatMap(_.routeIds).distinct.sorted.map(_.toString)
    FileUtils.writeStringToFile(file, routeIds.mkString("\n"), "UTF-8")
    println(s"\nsaved routeIds")
  }

  private def queryRouteInfos(): Seq[RouteInfo] = {
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
        queryRouteInfo("route", routeTagValue),
        queryRouteInfo("superroute", routeTagValue),
      )
    }
  }

  private def queryRouteInfo(typeTagValue: String, routeTagValue: String): RouteInfo = {
    println(s"type=$typeTagValue, route=$routeTagValue")
    val queryStart = s"[date:'2024-03-01T00:00:00Z'];relation['type'='$typeTagValue']['route'='$routeTagValue']"
    val nodeNetworkRouteIds = {
      val query = s"$queryStart['network:type'='node_network'];out ids;"
      queryRouteIds(query)
    }
    val noneNodeNetworkRouteIds = {
      val query = s"$queryStart['network:type'!='node_network'];out ids;"
      queryRouteIds(query)
    }
    RouteInfo(
      typeTagValue,
      routeTagValue,
      nodeNetworkRouteIds,
      noneNodeNetworkRouteIds
    )
  }

  private def queryRouteIds(query: String): Seq[Long] = {
    val xmlString = overpassQueryExecutor.execute(query)
    val xml = XML.loadString(xmlString)
    (xml.head \ "relation").map(r => (r \ "@id").text).map(_.toLong)
  }
}
