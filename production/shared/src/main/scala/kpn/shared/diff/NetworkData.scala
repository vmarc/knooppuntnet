package kpn.shared.diff

import kpn.shared.data.raw.RawRelation

case class NetworkData(
  relation: RawRelation,
  name: String
  // facts at network level
  // perhaps: nodeCount, routeCount, total length, ...
) {
  def id: Long = relation.id
}
