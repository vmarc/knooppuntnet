package kpn.api.common

case class Check(
  nodeId: Long,
  nodeName: String,
  expected: Long,
  actual: Long
)
