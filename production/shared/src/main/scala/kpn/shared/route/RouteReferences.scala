package kpn.shared.route

import kpn.shared.common.Reference

case class RouteReferences(networkReferences: Seq[Reference]= Seq.empty) {
  def isEmpty: Boolean = networkReferences.isEmpty
}
