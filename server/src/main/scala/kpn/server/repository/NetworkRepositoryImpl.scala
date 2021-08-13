package kpn.server.repository

import kpn.api.common.network.NetworkInfo
import kpn.core.gpx.GpxFile
import kpn.core.mongo.Database
import kpn.core.mongo.actions.networks.MongoQueryNetworkIds
import kpn.core.mongo.doc.NetworkDoc
import kpn.core.mongo.doc.NetworkInfoDoc
import kpn.core.util.Log
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

  override def oldSaveNetworkInfo(network: NetworkInfo): Unit = {
    throw new IllegalStateException("calling obsolete oldSaveNetworkInfo()")
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

  override def gpx(networkId: Long): Option[GpxFile] = {
    database.networkGpxs.findById(networkId, log)
  }

  override def saveGpxFile(gpxFile: GpxFile): Unit = {
    database.networkGpxs.save(gpxFile, log)
  }
}
