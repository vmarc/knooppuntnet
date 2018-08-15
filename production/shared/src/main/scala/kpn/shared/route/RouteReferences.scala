package kpn.shared.route

import kpn.shared.common.Reference

case class RouteReferences(networkReferences: Seq[Reference] = Seq()) {
  def isEmpty: Boolean = networkReferences.isEmpty
}
