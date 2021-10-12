package kpn.server.repository

import kpn.core.doc.NetworkDoc
import kpn.core.doc.NetworkInfoDoc
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQueryNetworkIds
import kpn.database.base.Database
import org.springframework.stereotype.Component

@Component
class NetworkRepositoryImpl(database: Database) extends NetworkRepository {

  private val log = Log(classOf[NetworkRepositoryImpl])

  override def allNetworkIds(): Seq[Long] = {
    database.networks.ids(log)
  }

  override def activeNetworkIds(): Seq[Long] = {
    new MongoQueryNetworkIds(database).execute()
  }

  override def findById(networkId: Long): Option[NetworkDoc] = {
    database.networks.findById(networkId, log)
  }

  override def save(networkDoc: NetworkDoc): Unit = {
    database.networks.save(networkDoc, log)
  }

  override def saveNetworkInfo(networkInfoDoc: NetworkInfoDoc): Unit = {
    database.networkInfos.save(networkInfoDoc, log)
  }

  override def bulkSave(networkDocs: Seq[NetworkDoc]): Unit = {
    database.networks.bulkSave(networkDocs, log)
  }

  override def delete(networkId: Long): Unit = {
    database.networks.delete(networkId, log)
    database.networkInfos.delete(networkId, log)
  }

}
