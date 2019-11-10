package kpn.core.replicate

import kpn.api.common.changes.ChangeSet
import kpn.api.common.common.Ref

case class ChangeSetNetworks(changeSet: ChangeSet, networks: Seq[Ref])
