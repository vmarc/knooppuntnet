package kpn.core.db

case class NodeDocViewResult(total_rows: Int, offset: Int, rows: Seq[NodeDocViewResultRow])
