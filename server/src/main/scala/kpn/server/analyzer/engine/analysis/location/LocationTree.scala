package kpn.server.analyzer.engine.analysis.location

case class LocationTree(name: String, children: Option[Seq[LocationTree]] = None)
