package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.data.Node

case class Nodes(delegate: Seq[Node] = Seq.empty) {
  override def toString: String = s"Nodes${delegate.map(_.id).mkString("(", ",", ")")}"
}
