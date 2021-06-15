package kpn.core.mongo.actions.statistics

case class ChangeSetCounts(
  years: Seq[ChangeSetCount2] = Seq.empty,
  months: Seq[ChangeSetCount2] = Seq.empty,
  days: Seq[ChangeSetCount2] = Seq.empty,
)
