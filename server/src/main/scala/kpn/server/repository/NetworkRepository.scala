package kpn.server.repository

import kpn.api.common.common.Reference
import kpn.core.doc.BaseNetworkDoc
import kpn.core.doc.NetworkDoc

trait NetworkRepository {

  def allNetworkIds(): Seq[Long]

  def baseNetworkIds(): Seq[Long]

  def activeNetworkIds(): Seq[Long]

  def findById(networkId: Long): Option[NetworkDoc]

  def save(networkDoc: NetworkDoc): Unit

  def bulkSave(networkDocs: Seq[NetworkDoc]): Unit

  def bulkSaveBaseNetworks(baseNetworkDocs: Seq[BaseNetworkDoc]): Unit

  def delete(networkId: Long): Unit

  def saveBaseNetwork(baseNetworkDoc: BaseNetworkDoc): Unit

  def findBaseNetworkById(networkId: Long): Option[BaseNetworkDoc]

  def nodeBaseNetworkReferences(nodeId: Long): Seq[Reference]

  def nodeNetworkReferences(nodeId: Long): Seq[Reference]

  def routeNetworkReferences(routeId: Long): Seq[Reference]
}
