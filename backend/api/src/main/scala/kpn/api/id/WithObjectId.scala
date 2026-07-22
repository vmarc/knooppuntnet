package kpn.api.id

import org.bson.types.ObjectId

trait WithObjectId extends Storable {
  def _id: ObjectId
}
