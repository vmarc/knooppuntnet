package kpn.api.common

import kpn.api.custom.NetworkType

case class NodeIntegrityCheckChange(networkType: NetworkType, before: Option[NodeIntegrityCheck], after: Option[NodeIntegrityCheck])
