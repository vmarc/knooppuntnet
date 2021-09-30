package kpn.server.repository

import kpn.api.common.network.NetworkInfo
import kpn.core.gpx.GpxFile
import kpn.core.doc.NetworkDoc
import kpn.core.doc.NetworkInfoDoc

trait NetworkRepository {

  def allNetworkIds(): Seq[Long]

  def activeNetworkIds(): Seq[Long]

  def findById(networkId: Long): Option[NetworkDoc]

  def oldSaveNetworkInfo(network: NetworkInfo): Unit

  def save(networkDoc: NetworkDoc): Unit

  def saveNetworkInfo(networkInfoDoc: NetworkInfoDoc): Unit

  def bulkSave(networkDocs: Seq[NetworkDoc]): Unit

  def gpx(networkId: Long): Option[GpxFile]

  def saveGpxFile(gpxFile: GpxFile): Unit

  def delete(networkId: Long): Unit

}
