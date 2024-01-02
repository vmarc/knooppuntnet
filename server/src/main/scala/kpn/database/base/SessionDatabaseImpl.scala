package kpn.database.base

import kpn.api.common.common.UserSession
import org.mongodb.scala.MongoDatabase

class SessionDatabaseImpl(val database: MongoDatabase) extends SessionDatabase {

  override def sessions: DatabaseCollection[UserSession] = {
    new DatabaseCollectionImpl(database.getCollection[UserSession]("sessions"))
  }

}
