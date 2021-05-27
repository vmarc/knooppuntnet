package kpn.core.mongo

import kpn.api.common.common.Ref
import kpn.api.custom.NetworkType
import org.mongodb.scala._

class MongoNodeRepositoryImpl(database: MongoDatabase) {

  def findLocationNodes(networkType: NetworkType, location: String, page: Int, pageSize: Int): (Long, Seq[NodeDoc2]) = {
    new MongoQueryLocationNodes(database: MongoDatabase).execute(networkType, location, page, pageSize)
  }

  def findNetworkReferences(nodeId: Long): Seq[Ref] = {
    new MongoQueryNode(database).findNetworkReferences(nodeId)
  }

  def findRouteReferences(nodeId: Long): Seq[Ref] = {
    new MongoQueryNode(database).findRouteReferences(nodeId)
  }
}
