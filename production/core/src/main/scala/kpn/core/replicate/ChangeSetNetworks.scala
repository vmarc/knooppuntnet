package kpn.core.replicate

import kpn.shared.changes.ChangeSet
import kpn.shared.common.Ref

case class ChangeSetNetworks(changeSet: ChangeSet, networks: Seq[Ref])
