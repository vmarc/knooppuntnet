package kpn.server.repository

import kpn.api.common.common.Reference
import kpn.core.doc.BaseNetworkDoc
import kpn.core.doc.NetworkDoc
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQueryBaseNetworkIds
import kpn.database.actions.networks.MongoQueryNetworkIds
import kpn.database.actions.nodes.MongoQueryNodeNetworkReferences
import kpn.database.actions.routes.MongoQueryRouteNetworkReferences
import kpn.database.base.Database
import org.springframework.stereotype.Component

@Component
class NetworkRepositoryImpl(database: Database) extends NetworkRepository {

  private val log = Log(classOf[NetworkRepositoryImpl])

  override def allNetworkIds(): Seq[Long] = {
    database.networks.ids(log)
  }

  override def baseNetworkIds(): Seq[Long] = {
    new MongoQueryBaseNetworkIds(database).execute()
  }

  override def activeNetworkIds(): Seq[Long] = {
    new MongoQueryNetworkIds(database).execute()
  }

  override def findById(networkId: Long): Option[NetworkDoc] = {
    database.networks.findById(networkId, log)
  }

  override def save(networkDoc: NetworkDoc): Unit = {
    database.networks.save(networkDoc, log)
  }

  override def bulkSave(networkDocs: Seq[NetworkDoc]): Unit = {
    database.networks.bulkSave(networkDocs, log)
  }

  override def bulkSaveBaseNetworks(baseNetworkDocs: Seq[BaseNetworkDoc]): Unit = {
    database.baseNetworks.bulkSave(baseNetworkDocs, log)
  }

  override def delete(networkId: Long): Unit = {
    database.networks.delete(networkId, log)
    database.baseNetworks.delete(networkId, log)
  }

  override def saveBaseNetwork(baseNetworkDoc: BaseNetworkDoc): Unit = {
    database.baseNetworks.save(baseNetworkDoc)
  }

  override def findBaseNetworkById(networkId: Long): Option[BaseNetworkDoc] = {
    database.baseNetworks.findById(networkId, log)
  }

  override def nodeNetworkReferences(routeId: Long): Seq[Reference] = {
    new MongoQueryNodeNetworkReferences(database).execute(routeId)
  }

  override def routeNetworkReferences(routeId: Long): Seq[Reference] = {
    new MongoQueryRouteNetworkReferences(database).execute(routeId)
  }
}
