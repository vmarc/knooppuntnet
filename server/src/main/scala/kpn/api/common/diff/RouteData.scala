package kpn.api.common.diff

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteType
import kpn.api.common.common.Ref
import kpn.api.common.data.MetaData
import kpn.api.common.data.Tagable
import kpn.api.common.data.Way
import kpn.api.common.route.RouteNode
import kpn.api.custom.Subset
import kpn.api.custom.Tag
import kpn.core.analysis.Facts
import kpn.core.doc.RouteDoc
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext

object RouteData {

  def from(routeDoc: RouteDoc): RouteData = {
    RouteData(
      relationId = routeDoc.id,
      meta = MetaData(
        version = routeDoc.version,
        timestamp = routeDoc.lastUpdated,
        changeSetId = routeDoc.changeSetId
      ),
      countries = routeDoc.summary.countries,
      routeTypes = routeDoc.summary.routeTypes,
      name = routeDoc.summary.name,
      networkNodes = routeDoc.nodes.nodes,
      ways = Seq.empty, // TODO all ways  in relation tree
      facts = routeDoc.facts,
      meters = routeDoc.summary.meters,
      locationAnalysis = routeDoc.locationAnalysis,
      tags = routeDoc.summary.tags
    )
  }

  def from(context: BaseRouteAnalysisContext): RouteData = {
    RouteData(
      context.relation.id,
      context.relation.toMeta,
      context.countries,
      context.routeTypes,
      context.routeNameAnalysis.name.getOrElse("no-name"),
      context.routeNodesAnalysis.nodes.map(_.toRouteNode),
      context.relation.wayMembers.map(_.way), // all ways  in relation tree
      context.facts,
      context.structure.nodeNetworkPaths.map(_.meters).sum,
      context.locationAnalysis,
      context.relation.tags
    )
  }
}

case class RouteData(
  relationId: Long,
  meta: MetaData,
  countries: Seq[Country],
  routeTypes: Seq[RouteType],
  name: String,
  networkNodes: Seq[RouteNode],
  ways: Seq[Way], // all ways  in relation tree
  facts: Seq[Fact],
  meters: Long,
  locationAnalysis: RouteLocationAnalysis,
  tags: Seq[Tag]
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
}
