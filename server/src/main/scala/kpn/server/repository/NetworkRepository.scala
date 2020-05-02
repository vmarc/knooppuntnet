package kpn.server.repository

import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkElements
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Subset
import kpn.core.gpx.GpxFile

trait NetworkRepository {

  def allNetworkIds(): Seq[Long]

  def network(networkId: Long): Option[NetworkInfo]

  def save(network: NetworkInfo): Unit

  def elements(networkId: Long): Option[NetworkElements]

  def saveElements(networkElements: NetworkElements): Unit

  def gpx(networkId: Long): Option[GpxFile]

  def saveGpxFile(gpxFile: GpxFile): Unit

  def networks(subset: Subset, stale: Boolean = true): Seq[NetworkAttributes]

  def delete(networkId: Long): Unit

}
