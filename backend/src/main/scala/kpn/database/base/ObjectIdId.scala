package kpn.database.base

import kpn.api.id.Storable
import org.bson.types.ObjectId

case class ObjectIdId(_id: ObjectId) extends Storable
