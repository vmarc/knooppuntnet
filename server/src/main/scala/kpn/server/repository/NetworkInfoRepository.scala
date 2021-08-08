package kpn.server.repository

import kpn.core.mongo.doc.NetworkInfoDoc

trait NetworkInfoRepository {

  def findById(networkId: Long): Option[NetworkInfoDoc]

}
