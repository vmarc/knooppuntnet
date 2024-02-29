package kpn.core.tools.next.database

import kpn.core.tools.next.domain.NextRoute
import kpn.core.tools.next.domain.NextRouteRelation
import kpn.core.tools.next.domain.NextRouteState
import kpn.database.base.DatabaseCollection
import kpn.database.base.DatabaseCollectionImpl
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.MongoDatabase

import scala.reflect.ClassTag

class NextDatabaseImpl(val database: MongoDatabase) extends NextDatabase {

  override def getCollection[T: ClassTag](collectionName: String): MongoCollection[T] = {
    database.getCollection[T](collectionName)
  }

  override def routes: DatabaseCollection[NextRoute] = {
    new DatabaseCollectionImpl(database.getCollection[NextRoute]("routes"))
  }

  override def routeRelations: DatabaseCollection[NextRouteRelation] = {
    new DatabaseCollectionImpl(database.getCollection[NextRouteRelation]("route-relations"))
  }

  override def routeStates: DatabaseCollection[NextRouteState] = {
    new DatabaseCollectionImpl(database.getCollection[NextRouteState]("route-states"))
  }
}
