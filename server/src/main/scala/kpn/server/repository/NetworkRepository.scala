package kpn.server.repository

import akka.util.Timeout
import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Subset
import kpn.core.gpx.GpxFile

trait NetworkRepository {

  def network(networkId: Long, timeout: Timeout): Option[NetworkInfo]

  def save(network: NetworkInfo): Boolean

  def gpx(networkId: Long, timeout: Timeout): Option[GpxFile]

  def saveGpxFile(gpxFile: GpxFile): Boolean

  def networks(subset: Subset, timeout: Timeout, stale: Boolean = true): Seq[NetworkAttributes]

  def delete(networkId: Long): Unit

}
