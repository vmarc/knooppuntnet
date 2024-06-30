package kpn.server.analyzer.engine.analysis.route.structure

case class NewSegment(id: Long, fromNodeId: Long, toNodeId: Long, links: Seq[RouteLinkWay])
