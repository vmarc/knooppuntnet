package kpn.server.analyzer.engine.changes.data

import kpn.api.id.Storable

case class BlacklistEntry(id: Long, name: String, reason: String) extends Storable
