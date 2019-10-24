package kpn.core.db.couch

case class ViewResult2(total_rows: Int, offset: Option[Int], rows: Seq[ViewResultRow2])
