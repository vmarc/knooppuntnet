package kpn.core.db

case class Change(seq: Long, id: String, changes: Seq[ChangeRevision])
