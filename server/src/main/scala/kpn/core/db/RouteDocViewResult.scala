package kpn.core.db

case class RouteDocViewResult(total_rows: Int, offset: Int, rows: Seq[RouteDocViewResultRow])
