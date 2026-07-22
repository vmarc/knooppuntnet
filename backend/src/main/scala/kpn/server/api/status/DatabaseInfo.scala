package kpn.server.api.status

import kpn.api.id.Storable

case class DatabaseInfo(sizes: DatabaseSizes, doc_count: Long) extends Storable
