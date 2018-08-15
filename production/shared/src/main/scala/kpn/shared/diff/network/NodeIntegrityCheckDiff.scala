package kpn.shared.diff.network

import kpn.shared.NodeIntegrityCheck

case class NodeIntegrityCheckDiff(before: Option[NodeIntegrityCheck], after: Option[NodeIntegrityCheck])
