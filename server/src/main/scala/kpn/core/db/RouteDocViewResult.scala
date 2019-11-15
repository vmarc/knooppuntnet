package kpn.core.db

case class RouteDocViewResult(total_rows: Long, offset: Long, rows: Seq[RouteDocViewResultRow])
