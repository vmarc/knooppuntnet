package kpn.api.common.diff

import kpn.api.common.common.Ref
import kpn.api.common.common.ToStringBuilder
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset

case class RouteData(
  country: Option[Country],
  networkType: NetworkType,
  networkScope: NetworkScope,
  relation: RawRelation,
  name: String,
  networkNodes: Seq[RawNode],
  nodes: Seq[RawNode], // all nodes  in hierarchy
  ways: Seq[RawWay], // all ways  in hierarchy
  relations: Seq[RawRelation], // all relations in hierarchy
  facts: Seq[Fact]
) {

  def id: Long = relation.id

  def toRef: Ref = Ref(id, name)

  def investigate: Boolean = facts.exists(_.isError)

  def subset: Option[Subset] = country.flatMap(c => Subset.of(c, networkType))

  override def toString: String = ToStringBuilder("RouteData").
    field("country", country).
    field("networkType", networkType).
    field("networkScope", networkScope).
    field("relation", relation).
    field("name", name).
    field("networkNodes", networkNodes).
    field("nodes", nodes).
    field("ways", ways).
    field("relations", relations).
    field("facts", facts).
    build
}
