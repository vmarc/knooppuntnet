package kpn.core.db.couch

case class ViewResult(total_rows: Int, offset: Int, rows: Seq[ViewResultRow])
