package kpn.database.base

import org.bson.types.ObjectId
import kpn.core.doc.Storable

case class ObjectIdId(_id: ObjectId) extends Storable
