package kpn.api.base

import org.bson.types.ObjectId

trait WithObjectId {
  def _id: ObjectId
}
