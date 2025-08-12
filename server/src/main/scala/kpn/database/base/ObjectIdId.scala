package kpn.database.base

import kpn.core.doc.Storable
import org.bson.types.ObjectId

case class ObjectIdId(_id: ObjectId) extends Storable
