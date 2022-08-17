package kpn.api.base

import org.mongodb.scala.bson.ObjectId

object MongoId {
  def apply(): MongoId = {
    MongoId(org.bson.types.ObjectId.get().toHexString)
  }
}

case class MongoId(oid: String) extends Ordered[MongoId] {
  override def compare(that: MongoId): Int = {
    oid.compareTo(that.oid)
  }

  def toObjectId: ObjectId = {
    new ObjectId(oid)
  }
}
