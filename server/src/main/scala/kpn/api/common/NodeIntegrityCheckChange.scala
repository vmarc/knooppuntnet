package kpn.api.common


case class NodeIntegrityCheckChange(networkType: NetworkType, before: Option[NodeIntegrityCheck], after: Option[NodeIntegrityCheck])
