package kpn.server.sync

import kpn.api.id.Storable
import org.bson.types.ObjectId

case class StampDoc(
  _id: Long,
  stamp: ObjectId
) extends Storable
