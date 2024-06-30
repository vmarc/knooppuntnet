package kpn.server.analyzer.engine.analysis.route.structure

case class NewSegment(id: Long, fromNodeId: Long, toNodeId: Long, paths: Seq[RoutePath])
