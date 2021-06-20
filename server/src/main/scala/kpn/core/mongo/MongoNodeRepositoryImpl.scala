package kpn.core.mongo

import kpn.api.common.common.Ref

class MongoNodeRepositoryImpl(database: Database) {

  def findNetworkReferences(nodeId: Long): Seq[Ref] = {
    new MongoQueryNode(database).findNetworkReferences(nodeId)
  }

  def findRouteReferences(nodeId: Long): Seq[Ref] = {
    new MongoQueryNode(database).findRouteReferences(nodeId)
  }
}
