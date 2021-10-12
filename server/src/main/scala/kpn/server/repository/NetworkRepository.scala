package kpn.server.repository

import kpn.core.doc.NetworkDoc
import kpn.core.doc.NetworkInfoDoc

trait NetworkRepository {

  def allNetworkIds(): Seq[Long]

  def activeNetworkIds(): Seq[Long]

  def findById(networkId: Long): Option[NetworkDoc]

  def save(networkDoc: NetworkDoc): Unit

  def saveNetworkInfo(networkInfoDoc: NetworkInfoDoc): Unit

  def bulkSave(networkDocs: Seq[NetworkDoc]): Unit

  def delete(networkId: Long): Unit

}
