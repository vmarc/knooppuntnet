package kpn.api.common

case class NodeIntegrityCheck(nodeName: String, nodeId: Long, actual: Long, expected: Long, failed: Boolean)
