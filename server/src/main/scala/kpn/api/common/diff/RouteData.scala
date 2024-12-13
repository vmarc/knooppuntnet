package kpn.api.common.diff

import kpn.api.common.Country
import kpn.api.common.NetworkType
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.common.Ref
import kpn.api.common.data.MetaData
import kpn.api.common.data.Tagable
import kpn.api.common.data.Way
import kpn.api.common.route.RouteNode
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.api.custom.Tag
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RouteData {

  def from(context: RouteDetailAnalysisContext): RouteData = {
    RouteData(
      context.relation.id,
      context.relation.toMeta,
      context.countries,
      context.networkTypes,
      context.routeNameAnalysis.name.getOrElse("no-name"),
      context.routeNodesAnalysis.nodes.map(_.toRouteNode),
      context.relation.wayMembers.map(_.way), // all ways  in hierarchy
      context.facts,
      context.structure.nodeNetworkPaths.map(_.meters).sum,
      context.locationAnalysis,
      context.tiles,
      context.relation.tags
    )
  }
}

case class RouteData(
  relationId: Long,
  meta: MetaData,
  countries: Seq[Country],
  networkTypes: Seq[NetworkType],
  name: String,
  networkNodes: Seq[RouteNode],
  ways: Seq[Way], // all ways  in hierarchy
  facts: Seq[Fact],
  meters: Long,
  locationAnalysis: RouteLocationAnalysis,
  tiles: Seq[String],
  tags: Seq[Tag]
) extends Tagable {

  def toRef: Ref = Ref(relationId, name)

  def investigate: Boolean = facts.exists(_.isError)

  def subsets: Seq[Subset] = {
    countries.flatMap { country =>
      networkTypes.flatMap { networkType =>
        Subset.of(country, networkType)
      }
    }
  }
}
