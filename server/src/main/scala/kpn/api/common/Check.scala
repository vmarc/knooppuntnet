package kpn.api.common

case class Check(
  nodeId: Long,
  nodeName: String,
  actual: Long,
  expected: Long
)
