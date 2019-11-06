package kpn.server.analyzer.engine.analysis.route

import kpn.shared.data.Node

case class Nodes(delegate: Seq[Node] = Seq.empty) {
  override def toString: String = "Nodes" + delegate.map(_.id).mkString("(", ",", ")")
}
