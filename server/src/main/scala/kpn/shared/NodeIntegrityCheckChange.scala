package kpn.shared

case class NodeIntegrityCheckChange(networkType: NetworkType, before: Option[NodeIntegrityCheck], after: Option[NodeIntegrityCheck])
