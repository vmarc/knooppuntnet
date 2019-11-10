package kpn.api.common

case class NetworkIntegrityCheckFailed(count: Int, checks: Seq[NodeIntegrityCheck])
