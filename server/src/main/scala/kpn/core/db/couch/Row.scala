package kpn.core.db.couch

case class Row[K, V](id: String, key: K, value: V)
