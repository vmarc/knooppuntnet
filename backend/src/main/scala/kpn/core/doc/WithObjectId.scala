package kpn.core.doc

import org.bson.types.ObjectId

trait WithObjectId extends Storable {
  def _id: ObjectId
}
