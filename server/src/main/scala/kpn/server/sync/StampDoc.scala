package kpn.server.sync

import org.bson.types.ObjectId

case class StampDoc(
  _id: Long,
  stamp: ObjectId
)
