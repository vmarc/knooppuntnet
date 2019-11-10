package kpn.api.common

case class NodeIntegrityCheck(nodeName: String, nodeId: Long, actual: Int, expected: Int, failed: Boolean)
