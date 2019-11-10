package kpn.api.common.route

import kpn.api.common.common.Reference

case class RouteReferences(networkReferences: Seq[Reference] = Seq.empty) {
  def isEmpty: Boolean = networkReferences.isEmpty
}
