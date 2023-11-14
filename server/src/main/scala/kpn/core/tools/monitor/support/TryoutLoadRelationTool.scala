package kpn.core.tools.monitor

import kpn.core.common.RelationUtil
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelation

import scala.xml.XML

object TryoutLoadRelationTool {

  private val largeRoutes = Seq(
    "LAW-2" -> 532494L,
    "LAW-3" -> 2801085L,
    "LAW-5-1" -> 1959162L,
    "LAW-5-2" -> 9237672L,
    "LAW-5-3" -> 4097849L,
    "LAW-9-1" -> 312993L,
    "LAW-9-2" -> 156951L,
    "E2" -> 1976033L,
    "E3" -> 6585798L,
    "E4" -> 171821L,
    "E5" -> 12844449L,
    "E7" -> 11865350L,
    "E9" -> 1956185L,
    "E12" -> 12744629L,
  )

  def main(args: Array[String]): Unit = {
    val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
    new TryoutLoadRelationTool(overpassQueryExecutor).tryout()
  }
}

class TryoutLoadRelationTool(overpassQueryExecutor: OverpassQueryExecutor) {

  def tryout(): Unit = {
    println("|name|relationId|relations|ways|loaded|xml|geojson|")
    println("|----|----------|---------|----|------|---|-------|")
    TryoutLoadRelationTool.largeRoutes.foreach { case (name, relationId) =>
      print(s"|$name|$relationId")
      val t1 = System.currentTimeMillis()
      val xmlString = overpassQueryExecutor.executeQuery(None, QueryRelation(relationId))
      val xml = XML.loadString(xmlString)
      val rawData = new Parser().parse(xml.head)
      val data = new DataBuilder(rawData).data
      val relationOption = data.relations.get(relationId)
      val t2 = System.currentTimeMillis()
      relationOption match {
        case None => print("not found")
        case Some(relation) =>
          val allRelations = RelationUtil.relationsInRelation(relation)
          val allWayMembers = allRelations.flatMap(relation => relation.wayMembers)
          print(s"|${allRelations.size}")
          print(s"|${allWayMembers.size}")
          print(s"|${t2 - t1}ms")
          val nodeMap = allWayMembers.flatMap(_.way.nodes).map(node => node.id -> node).toMap
          val coordinateString = nodeMap.values.map(node => s"[${node.latitude},${node.longitude}]").mkString(",")
          print(s"|${xmlString.length / 1000}K")
          print(s"|${coordinateString.length / 1000}K|")
      }
      println()
    }
  }
}
