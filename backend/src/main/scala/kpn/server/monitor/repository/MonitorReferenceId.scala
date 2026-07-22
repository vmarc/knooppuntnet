package kpn.server.monitor.repository

import kpn.api.id.Storable
import org.bson.types.ObjectId

case class MonitorReferenceId(
  _id: ObjectId,
  relationId: Option[Long],
) extends Storable
