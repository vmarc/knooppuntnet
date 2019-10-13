package kpn.server.analyzer.engine.analysis.location

case class LocationTree(name: String, children: Seq[LocationTree] = Seq.empty)
