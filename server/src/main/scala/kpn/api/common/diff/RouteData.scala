package kpn.api.common.diff

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteType
import kpn.api.common.common.Ref
import kpn.api.common.data.Tagable
import kpn.api.common.data.raw.Raw
import kpn.api.common.route.RouteNode
import kpn.api.custom.Subset
import kpn.api.custom.Tag
import kpn.core.analysis.Facts
import kpn.core.doc.RouteDoc
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext

object RouteData {

  def from(routeDoc: RouteDoc): RouteData = {
    RouteData(
      relationId = routeDoc._id,
      raw = routeDoc.base.raw,
      countries = routeDoc.base.summary.countries,
      routeTypes = routeDoc.base.summary.routeTypes,
      name = routeDoc.base.summary.name,
      networkNodes = routeDoc.base.nodes.nodes,
      facts = routeDoc.facts,
      meters = routeDoc.base.summary.meters,
      locationAnalysis = routeDoc.base.locationAnalysis
    )
  }

  def from(context: BaseRouteAnalysisContext): RouteData = {
    RouteData(
      context.relation.id,
      context.relation.raw,
      context.countries,
      context.routeTypes,
      context.routeNameAnalysis.name.getOrElse("no-name"),
      context.routeNodesAnalysis.nodes.map(_.toRouteNode),
      context.facts,
      context.structure.nodeNetworkPaths.map(_.meters).sum,
      context.locationAnalysis
    )
  }
}

case class RouteData(
  relationId: Long,
  raw: Raw,
  countries: Seq[Country],
  routeTypes: Seq[RouteType],
  name: String,
  networkNodes: Seq[RouteNode],
  facts: Seq[Fact],
  meters: Long,
  locationAnalysis: RouteLocationAnalysis
) extends Tagable {

  def toRef: Ref = Ref(relationId, name)

  def investigate: Boolean = facts.exists(Facts.isError)

  def subsets: Seq[Subset] = {
    countries.flatMap { country =>
      routeTypes.flatMap { routeType =>
        Subset.of(country, routeType)
      }
    }
  }

  def tags: Seq[Tag] = raw.tags
}
