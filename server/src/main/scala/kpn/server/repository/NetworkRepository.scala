package kpn.server.repository

import kpn.core.doc.BaseNetworkDoc
import kpn.core.doc.NetworkDoc

trait NetworkRepository {

  def allNetworkIds(): Seq[Long]

  def baseNetworkIds(): Seq[Long]

  def activeNetworkIds(): Seq[Long]

  def findById(networkId: Long): Option[NetworkDoc]

  def save(networkDoc: NetworkDoc): Unit

  def bulkSave(networkDocs: Seq[NetworkDoc]): Unit

  def delete(networkId: Long): Unit

  def saveBaseNetwork(baseNetworkDoc: BaseNetworkDoc): Unit

  def findBaseNetworkById(networkId: Long): Option[BaseNetworkDoc]
}
