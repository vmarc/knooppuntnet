package kpn.shared.node

case class NodeNetworkIntegrityCheck(failed: Boolean, expected: Int, actual: Int)
