package kpn.api.common.diff;

import kpn.api.common.Country;
import kpn.api.common.Fact;
import kpn.api.common.RouteType;
import kpn.api.common.common.Ref;
import kpn.api.common.data.Tagable;
import kpn.api.common.data.raw.Raw;
import kpn.api.common.route.RouteNode;
import kpn.api.custom.Subset;
import kpn.api.custom.Tag;

import com.google.common.collect.ImmutableList;

public record RouteData(
  Long relationId,
  Raw raw,
  ImmutableList<Country> countries,
  ImmutableList<RouteType> routeTypes,
  String name,
  ImmutableList<RouteNode> networkNodes,
  ImmutableList<Fact> facts,
  Long meters
) implements Tagable {

  public Ref toRef() {
    return new Ref(relationId, name);
  }

//  public boolean investigate() {
//    return facts.stream().anyMatch(Facts::isError);
//  }

  public ImmutableList<Subset> subsets() {
    return
      countries.stream().flatMap(
        country -> routeTypes.stream()
          .flatMap(routeType -> Subset.of(country, routeType).stream())
      ).collect(ImmutableList.toImmutableList());
  }

  public ImmutableList<Tag> tags() {
    return raw.tags();
  }
  /*
  def tags: Seq[Tag] = raw.tags
   */
}

/* TODO migrate

object RouteData {

  def from(routeDoc: RouteDoc): RouteData = {
    RouteData(
      relationId = routeDoc._id,
      raw = routeDoc.base.raw,
      countries = routeDoc.base.countries,
      routeTypes = routeDoc.base.routeTypes,
      name = routeDoc.base.name,
      networkNodes = routeDoc.base.nodes.nodes,
      facts = routeDoc.facts,
      meters = routeDoc.base.meters
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
      context.structure.nodeNetworkPaths.map(_.meters).sum
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
  meters: Long
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

*/
