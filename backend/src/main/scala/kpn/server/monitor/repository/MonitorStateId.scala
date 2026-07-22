package kpn.server.monitor.repository

import kpn.api.id.Storable
import org.bson.types.ObjectId

case class MonitorStateId(
  _id: ObjectId,
  relationId: Long,
) extends Storable
