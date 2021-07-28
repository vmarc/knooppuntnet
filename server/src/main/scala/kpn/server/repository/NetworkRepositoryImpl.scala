package kpn.server.repository

import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Subset
import kpn.core.database.doc.GpxDoc
import kpn.core.database.doc.CouchNetworkDoc
import kpn.core.database.doc.NetworkElementsDoc
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.database.views.analyzer.NetworkView
import kpn.core.db._
import kpn.core.gpx.GpxFile
import kpn.core.mongo.Database
import kpn.core.mongo.actions.subsets.MongoQuerySubsetNetworks
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.changes.NetworkElements
import org.springframework.stereotype.Component

@Component
class NetworkRepositoryImpl(
  database: Database,
  // old
  analysisDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends NetworkRepository {

  private val log = Log(classOf[NetworkRepositoryImpl])

  override def allNetworkIds(): Seq[Long] = {
    if (mongoEnabled) {
      database.networks.ids(log)
    }
    else {
      DocumentView.allNetworkIds(analysisDatabase)
    }
  }

  override def network(networkId: Long): Option[NetworkInfo] = {
    if (mongoEnabled) {
      database.networks.findById(networkId, log)
    }
    else {
      analysisDatabase.docWithId(networkKey(networkId), classOf[CouchNetworkDoc]).map(_.network)
    }
  }

  override def elements(networkId: Long): Option[NetworkElements] = {
    if (mongoEnabled) {
      database.networkElements.findById(networkId, log)
    }
    else {
      analysisDatabase.docWithId(networkElementsKey(networkId), classOf[NetworkElementsDoc]).map(_.networkElements)
    }
  }

  override def saveElements(networkElements: NetworkElements): Unit = {
    if (mongoEnabled) {
      database.networkElements.save(networkElements, log)
    }
    else {
      val key = networkElementsKey(networkElements._id)
      analysisDatabase.save(NetworkElementsDoc(key, networkElements))
    }
  }

  override def save(network: NetworkInfo): Unit = {
    if (mongoEnabled) {
      database.networks.save(network, log)
    }
    else {
      log.debugElapsed {
        val key = networkKey(network.id)
        analysisDatabase.save(CouchNetworkDoc(key, network))
        (s"Save network ${network.id}", ())
      }
    }
  }

  override def delete(networkId: Long): Unit = {
    if (mongoEnabled) {
      database.networks.delete(networkId, log)
    }
    else {
      analysisDatabase.deleteDocWithId(networkKey(networkId))
      analysisDatabase.deleteDocWithId(networkElementsKey(networkId))
    }
  }

  private def networkKey(networkId: Long): String = s"${KeyPrefix.Network}:$networkId"

  private def networkElementsKey(networkId: Long): String = s"${KeyPrefix.NetworkElements}:$networkId"

  override def gpx(networkId: Long): Option[GpxFile] = {
    if (mongoEnabled) {
      database.networkGpxs.findById(networkId, log)
    }
    else {
      analysisDatabase.docWithId(gpxKey(networkId), classOf[GpxDoc]).map(_.file)
    }
  }

  override def saveGpxFile(gpxFile: GpxFile): Unit = {
    if (mongoEnabled) {
      database.networkGpxs.save(gpxFile, log)
    }
    else {
      val key = gpxKey(gpxFile._id)
      analysisDatabase.save(GpxDoc(key, gpxFile))
    }
  }

  private def gpxKey(networkId: Long): String = s"${KeyPrefix.NetworkGpx}:$networkId"

  override def networks(subset: Subset, stale: Boolean): Seq[NetworkAttributes] = {
    if (mongoEnabled) {
      new MongoQuerySubsetNetworks(database).execute(subset)
    }
    else {
      NetworkView.query(analysisDatabase, subset, stale)
    }
  }
}
