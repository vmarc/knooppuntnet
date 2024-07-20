package kpn.api.common.diff

import kpn.api.common.common.Ref
import kpn.api.common.data.MetaData
import kpn.api.common.route.RouteNode
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RouteData {

  def from(context: RouteDetailAnalysisContext): RouteData = {
    RouteData(
      context.relation.id,
      context.relation.toMeta,
      context.country.toSeq,
      context.networkTypes,
      //      context.networkScope: NetworkScope,
      //      context.relation: RawRelation,
      context.routeNameAnalysis.name.getOrElse("no-name"),
      context.nodes.nodes.map(_.toRouteNode),
      //      context.nodes: Seq[Node], // all nodes  in hierarchy
      //      context.ways: Seq[RawWay], // all ways  in hierarchy
      //      context.relations: Seq[RawRelation], // all relations in hierarchy
      context.facts
    )
  }
}

case class RouteData(
  relationId: Long,
  meta: MetaData,
  countries: Seq[Country],
  networkTypes: Seq[NetworkType],
  //  networkScope: NetworkScope,
  //  relation: RawRelation,
  name: String,
  networkNodes: Seq[RouteNode],
  //  nodes: Seq[Node], // all nodes  in hierarchy
  //  ways: Seq[RawWay], // all ways  in hierarchy
  //  relations: Seq[RawRelation], // all relations in hierarchy
  facts: Seq[Fact]
) {

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
