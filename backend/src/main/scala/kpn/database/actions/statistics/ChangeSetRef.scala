package kpn.database.actions.statistics

import kpn.api.id.Storable

case class ChangeSetRef(
  replicationNumber: Long,
  changeSetId: Long
) extends Storable
