package kpn.server.repository

import kpn.api.common.common.Reference
import kpn.api.common.network.NetworkAttributes
import kpn.api.custom.Subset
import kpn.core.doc.BaseNetworkDoc
import kpn.core.doc.NetworkDoc
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQueryBaseNetworkIds
import kpn.database.actions.networks.MongoQueryNetworkIds
import kpn.database.actions.networks.MongoQuerySubsetNetworks
import kpn.database.actions.nodes.MongoQueryNodeBaseNetworkReferences
import kpn.database.actions.nodes.MongoQueryNodeNetworkReferences
import kpn.database.actions.routes.MongoQueryRouteNetworkReferences
import kpn.database.base.Database
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web", "analysis"))
class NetworkRepository(database: Database) {

  private val log = Log(classOf[NetworkRepository])

  def allNetworkIds(): Seq[Long] = {
    database.networks.ids(log)
  }

  def baseNetworkIds(): Seq[Long] = {
    new MongoQueryBaseNetworkIds(database).execute()
  }

  def activeNetworkIds(): Seq[Long] = {
    new MongoQueryNetworkIds(database).execute()
  }

  def findById(networkId: Long): Option[NetworkDoc] = {
    database.networks.findById(networkId, log)
  }

  def save(networkDoc: NetworkDoc): Unit = {
    database.networks.save(networkDoc, log)
  }

  def bulkSave(networkDocs: Seq[NetworkDoc]): Unit = {
    database.networks.bulkSave(networkDocs, log)
  }

  def bulkSaveBaseNetworks(baseNetworkDocs: Seq[BaseNetworkDoc]): Unit = {
    database.baseNetworks.bulkSave(baseNetworkDocs, log)
  }

  def delete(networkId: Long): Unit = {
    database.networks.delete(networkId, log)
    database.baseNetworks.delete(networkId, log)
  }

  def saveBaseNetwork(baseNetworkDoc: BaseNetworkDoc): Unit = {
    database.baseNetworks.save(baseNetworkDoc)
  }

  def findBaseNetworkById(networkId: Long): Option[BaseNetworkDoc] = {
    database.baseNetworks.findById(networkId, log)
  }

  def nodeBaseNetworkReferences(nodeId: Long): Seq[Reference] = {
    new MongoQueryNodeBaseNetworkReferences(database).execute(nodeId)
  }

  def nodeNetworkReferences(nodeId: Long): Seq[Reference] = {
    new MongoQueryNodeNetworkReferences(database).execute(nodeId)
  }

  def routeNetworkReferences(routeId: Long): Seq[Reference] = {
    new MongoQueryRouteNetworkReferences(database).execute(routeId)
  }

  def subsetNetworks(subset: Subset): Seq[NetworkAttributes] = {
    new MongoQuerySubsetNetworks(database).execute(subset)
  }
}
