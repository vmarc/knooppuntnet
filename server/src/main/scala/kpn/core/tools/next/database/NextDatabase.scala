package kpn.core.tools.next.database

import kpn.core.tools.next.domain.NextRoute
import kpn.core.tools.next.domain.NextRouteRelation
import kpn.core.tools.next.domain.NextRouteState
import kpn.database.base.DatabaseCollection
import org.mongodb.scala.MongoCollection

import scala.reflect.ClassTag

trait NextDatabase {

  def getCollection[T: ClassTag](collectionName: String): MongoCollection[T]

  def routes: DatabaseCollection[NextRoute]

  def routeRelations: DatabaseCollection[NextRouteRelation]

  def routeStates: DatabaseCollection[NextRouteState]
}
