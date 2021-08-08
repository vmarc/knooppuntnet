package kpn.server.repository

import kpn.api.common.network.NetworkAttributes
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.doc.NetworkInfoDoc
import org.springframework.stereotype.Component

@Component
class NetworkInfoRepositoryImpl(database: Database) extends NetworkInfoRepository {

  override def findById(networkId: Long): Option[NetworkInfoDoc] = {
    database.networkInfos.findById(networkId)
  }

  override def networks(subset: Subset, stale: Boolean = true): Seq[NetworkAttributes] = {
    throw new IllegalStateException("not implemented")
  }

}
