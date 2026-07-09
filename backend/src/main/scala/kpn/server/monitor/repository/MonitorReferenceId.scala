package kpn.server.monitor.repository

import kpn.core.doc.Storable
import org.bson.types.ObjectId

case class MonitorReferenceId(
  _id: ObjectId,
  relationId: Option[Long],
) extends Storable
