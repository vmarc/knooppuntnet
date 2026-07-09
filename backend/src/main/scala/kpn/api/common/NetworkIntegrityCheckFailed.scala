package kpn.api.common

case class NetworkIntegrityCheckFailed(count: Long, checks: Seq[NodeIntegrityCheck])
