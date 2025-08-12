package kpn.server.monitor.repository

import kpn.core.doc.Storable
import org.bson.types.ObjectId

case class MonitorStateId(
  _id: ObjectId,
  relationId: Long,
) extends Storable
