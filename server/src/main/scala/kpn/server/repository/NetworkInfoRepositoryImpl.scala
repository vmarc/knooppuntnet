package kpn.server.repository

import kpn.core.mongo.Database
import kpn.core.mongo.doc.NetworkInfoDoc
import org.springframework.stereotype.Component

@Component
class NetworkInfoRepositoryImpl(database: Database) extends NetworkInfoRepository {

  def findById(networkId: Long): Option[NetworkInfoDoc] = {
    database.networkInfos.findById(networkId)
  }

}
