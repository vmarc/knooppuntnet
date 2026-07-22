package kpn.server.api.status

import kpn.core.doc.Storable

case class DatabaseInfo(sizes: DatabaseSizes, doc_count: Long) extends Storable
