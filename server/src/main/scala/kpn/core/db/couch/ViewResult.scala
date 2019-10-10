package kpn.core.db.couch

case class ViewResult[K, V](total_rows: Int, offset: Int, rows: Seq[Row[K, V]])

