package kpn.shared

case class NetworkIntegrityCheckFailed(count: Int, checks: Seq[NodeIntegrityCheck])
