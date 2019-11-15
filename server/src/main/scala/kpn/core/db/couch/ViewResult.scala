package kpn.core.db.couch

case class ViewResult(total_rows: Long, offset: Long, rows: Seq[ViewResultRow])
