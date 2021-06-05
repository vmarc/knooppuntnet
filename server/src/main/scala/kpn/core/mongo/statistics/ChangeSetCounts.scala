package kpn.core.mongo.statistics

case class ChangeSetCounts(
  years: Seq[ChangeSetCount2] = Seq.empty,
  months: Seq[ChangeSetCount2] = Seq.empty,
  days: Seq[ChangeSetCount2] = Seq.empty,
)
