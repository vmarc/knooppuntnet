package kpn.core.engine.analysis

import kpn.shared.data.Node

case class Nodes(delegate: Seq[Node] = Seq()) {
  override def toString: String = "Nodes" + delegate.map(_.id).mkString("(", ",", ")")
}
