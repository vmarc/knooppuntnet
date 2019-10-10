package kpn.shared.diff

import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.common.Ref
import kpn.shared.common.ToStringBuilder
import kpn.shared.data.raw.RawNode
import kpn.shared.data.raw.RawRelation
import kpn.shared.data.raw.RawWay

case class RouteData(
  country: Option[Country],
  networkType: NetworkType,
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
    field("relation", relation).
    field("name", name).
    field("networkNodes", networkNodes).
    field("nodes", nodes).
    field("ways", ways).
    field("relations", relations).
    field("facts", facts).
    build
}
