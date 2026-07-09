package kpn.server.analyzer.engine.changes.data

import kpn.core.doc.Storable

case class BlacklistEntry(id: Long, name: String, reason: String) extends Storable
