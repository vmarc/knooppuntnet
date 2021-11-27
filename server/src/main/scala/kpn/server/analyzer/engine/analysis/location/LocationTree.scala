package kpn.server.analyzer.engine.analysis.location

case class LocationTree(name: String, children: Option[Seq[LocationTree]] = None) {
  def childLocations: Seq[LocationTree] = children.toSeq.flatten
}
