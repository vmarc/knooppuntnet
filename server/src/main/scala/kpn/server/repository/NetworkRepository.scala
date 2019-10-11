package kpn.server.repository

import akka.util.Timeout
import kpn.core.gpx.GpxFile
import kpn.shared.Subset
import kpn.shared.network.NetworkAttributes
import kpn.shared.network.NetworkInfo
import kpn.shared.network.NetworkMapInfo

trait NetworkRepository {

  def network(networkId: Long, timeout: Timeout): Option[NetworkInfo]

  def save(network: NetworkInfo): Boolean

  def gpx(networkId: Long, timeout: Timeout): Option[GpxFile]

  def saveGpxFile(gpxFile: GpxFile): Boolean

  def networks(subset: Subset, timeout: Timeout, stale: Boolean = true): Seq[NetworkAttributes]

  def networksMap(country: String, networkType: String, timeout: Timeout, stale: Boolean = true): Seq[NetworkMapInfo]

  def delete(networkId: Long): Unit

}
