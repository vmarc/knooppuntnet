package kpn.api.common.diff.network

import kpn.api.common.NodeIntegrityCheck

case class NodeIntegrityCheckDiff(before: Option[NodeIntegrityCheck], after: Option[NodeIntegrityCheck])
