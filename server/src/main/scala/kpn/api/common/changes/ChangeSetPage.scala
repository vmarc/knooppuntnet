package kpn.api.common.changes

case class ChangeSetPage(
  replicationNumbers: Seq[Long],
  detail: Option[ChangeSetDetail]
)
