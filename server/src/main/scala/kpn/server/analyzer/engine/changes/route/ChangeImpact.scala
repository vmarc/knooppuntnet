package kpn.server.analyzer.engine.changes.route

case class ChangeImpact(
  impactedNodeIds: Seq[Long],
  impactedTiles: Seq[String]
)
