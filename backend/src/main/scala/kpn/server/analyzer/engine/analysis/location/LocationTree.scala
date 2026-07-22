package kpn.server.analyzer.engine.analysis.location

import kpn.api.id.Storable

case class LocationTree(name: String, children: Option[Seq[LocationTree]] = None) extends Storable {
  def childLocations: Seq[LocationTree] = children.toSeq.flatten
}
