package kpn.server.analyzer.engine.monitor.analysis

/*
  Sequence of indexes that refer to positions in the reference coordinate sequence.
  This allows to efficiently reference the actual coordinate data without duplicating it.
 */
case class ReferenceCoordinateSequence(
  indexes: Seq[Int]
)
