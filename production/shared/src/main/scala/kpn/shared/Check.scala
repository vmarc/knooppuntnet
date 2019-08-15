package kpn.shared

case class Check(
  nodeId: Long,
  nodeName: String,
  actual: Int,
  expected: Int
)
