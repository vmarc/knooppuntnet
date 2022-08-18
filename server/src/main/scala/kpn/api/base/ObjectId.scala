package kpn.api.base

object ObjectId {
  def apply(): ObjectId = {
    ObjectId(org.bson.types.ObjectId.get().toHexString)
  }
}

case class ObjectId(oid: String) extends Ordered[ObjectId] {
  override def compare(that: ObjectId): Int = {
    oid.compareTo(that.oid)
  }

  def raw: org.bson.types.ObjectId = {
    new org.bson.types.ObjectId(oid)
  }
}
