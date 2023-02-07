package kpn.server.monitor.route

import kpn.api.common.monitor.MonitorRouteInfoPage
import kpn.core.common.RelationUtil
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelation
import org.springframework.stereotype.Component

import scala.xml.XML

@Component
class MonitorRouteInfoBuilder(overpassQueryExecutor: OverpassQueryExecutor) {

  def build(routeRelationId: Long): MonitorRouteInfoPage = {
    val xmlString = overpassQueryExecutor.executeQuery(None, QueryRelation(routeRelationId))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    val data = new DataBuilder(rawData).data
    data.relations.get(routeRelationId) match {
      case None => MonitorRouteInfoPage("TODO MON", routeRelationId)
      case Some(relation) =>

        val relations = RelationUtil.relationsInRelation(relation)
        val nodeCount = relations.map(_.nodeMembers.size).sum
        val wayCount = relations.map(_.wayMembers.size).sum
        val meters = relations.flatMap(_.wayMembers.map(_.way.length)).sum
        val km = Math.round(meters.toDouble / 1000)

        MonitorRouteInfoPage(
          "TODO",
          routeRelationId,
          relation.tags("name"),
          relation.tags("operator"),
          relation.tags("ref"),
          nodeCount,
          wayCount,
          relations.size,
          km
        )
    }
  }
}
