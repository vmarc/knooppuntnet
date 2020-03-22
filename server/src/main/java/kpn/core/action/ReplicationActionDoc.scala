package kpn.core.action

import kpn.core.database.doc.Doc

case class ReplicationActionDoc(_id: String, replication: ReplicationAction, _rev: Option[String] = None) extends Doc
