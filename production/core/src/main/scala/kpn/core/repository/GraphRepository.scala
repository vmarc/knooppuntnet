package kpn.core.repository

import kpn.shared.NetworkType

import scalax.collection.edge.WLUnDiEdge

trait GraphRepository {
  def edges(networkType: NetworkType): Seq[WLUnDiEdge[Long]]
}
