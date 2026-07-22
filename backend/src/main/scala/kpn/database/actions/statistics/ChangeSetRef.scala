package kpn.database.actions.statistics

import kpn.core.doc.Storable

case class ChangeSetRef(
  replicationNumber: Long,
  changeSetId: Long
) extends Storable
