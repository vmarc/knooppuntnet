package kpn.server.repository

import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Subset
import kpn.core.gpx.GpxFile
import kpn.core.mongo.doc.NetworkDoc

trait NetworkRepository {

  def allNetworkIds(): Seq[Long]

  def activeNetworkIds(): Seq[Long]

  def findById(networkId: Long): Option[NetworkDoc]

  def oldSaveNetworkInfo(network: NetworkInfo): Unit

  def save(networkDoc: NetworkDoc): Unit

  def bulkSave(networkDocs: Seq[NetworkDoc]): Unit

  def gpx(networkId: Long): Option[GpxFile]

  def saveGpxFile(gpxFile: GpxFile): Unit

  def networks(subset: Subset, stale: Boolean = true): Seq[NetworkAttributes]

  def delete(networkId: Long): Unit

}
