package kpn.server.repository

import kpn.api.common.network.NetworkAttributes
import kpn.api.custom.Subset
import kpn.core.mongo.doc.NetworkInfoDoc

trait NetworkInfoRepository {

  def findById(networkId: Long): Option[NetworkInfoDoc]

  def networks(subset: Subset, stale: Boolean = true): Seq[NetworkAttributes]

}
