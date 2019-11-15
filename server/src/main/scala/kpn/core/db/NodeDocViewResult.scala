package kpn.core.db

case class NodeDocViewResult(total_rows: Long, offset: Long, rows: Seq[NodeDocViewResultRow])
