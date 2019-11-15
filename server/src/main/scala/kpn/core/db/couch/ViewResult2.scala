package kpn.core.db.couch

case class ViewResult2(total_rows: Long, offset: Option[Long], rows: Seq[ViewResultRow2])
