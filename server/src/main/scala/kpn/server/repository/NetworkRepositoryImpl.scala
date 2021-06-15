package kpn.server.repository

import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Subset
import kpn.core.database.Database
import kpn.core.database.doc.GpxDoc
import kpn.core.database.doc.NetworkDoc
import kpn.core.database.doc.NetworkElementsDoc
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.database.views.analyzer.NetworkView
import kpn.core.db._
import kpn.core.gpx.GpxFile
import kpn.core.mongo.actions.base.MongoDelete
import kpn.core.mongo.actions.base.MongoFindById
import kpn.core.mongo.actions.base.MongoQueryIds
import kpn.core.mongo.actions.base.MongoSave
import kpn.core.mongo.actions.subsets.MongoQuerySubsetNetworks
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.changes.NetworkElements
import org.mongodb.scala.MongoDatabase
import org.springframework.stereotype.Component

@Component
class NetworkRepositoryImpl(
  // old
  analysisDatabase: Database,
  // new
  mongoEnabled: Boolean,
  mongoDatabase: MongoDatabase
) extends NetworkRepository {

  private val log = Log(classOf[NetworkRepositoryImpl])

  override def allNetworkIds(): Seq[Long] = {
    if (mongoEnabled) {
      new MongoQueryIds(mongoDatabase).execute("networks")
    }
    else {
      DocumentView.allNetworkIds(analysisDatabase)
    }
  }

  override def network(networkId: Long): Option[NetworkInfo] = {
    if (mongoEnabled) {
      new MongoFindById(mongoDatabase).execute("networks", networkId)
    }
    else {
      analysisDatabase.docWithId(networkKey(networkId), classOf[NetworkDoc]).map(_.network)
    }
  }

  override def elements(networkId: Long): Option[NetworkElements] = {
    if (mongoEnabled) {
      new MongoFindById(mongoDatabase).execute("network-elements", networkId)
    }
    else {
      analysisDatabase.docWithId(networkElementsKey(networkId), classOf[NetworkElementsDoc]).map(_.networkElements)
    }
  }

  override def saveElements(networkElements: NetworkElements): Unit = {
    if (mongoEnabled) {
      new MongoSave(mongoDatabase).execute("network-elements", networkElements)
    }
    else {
      val key = networkElementsKey(networkElements._id)
      analysisDatabase.save(NetworkElementsDoc(key, networkElements))
    }
  }

  override def save(network: NetworkInfo): Unit = {
    if (mongoEnabled) {
      new MongoSave(mongoDatabase).execute("networks", network)
    }
    else {
      log.debugElapsed {
        val key = networkKey(network.id)
        analysisDatabase.save(NetworkDoc(key, network))
        (s"Save network ${network.id}", ())
      }
    }
  }

  override def delete(networkId: Long): Unit = {
    if (mongoEnabled) {
      new MongoDelete(mongoDatabase).execute("networks", networkId)
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
      new MongoFindById(mongoDatabase).execute("network-gpxs", networkId)
    }
    else {
      analysisDatabase.docWithId(gpxKey(networkId), classOf[GpxDoc]).map(_.file)
    }
  }

  override def saveGpxFile(gpxFile: GpxFile): Unit = {
    if (mongoEnabled) {
      new MongoSave(mongoDatabase).execute("network-gpxs", gpxFile)
    }
    else {
      val key = gpxKey(gpxFile._id)
      analysisDatabase.save(GpxDoc(key, gpxFile))
    }
  }

  private def gpxKey(networkId: Long): String = s"${KeyPrefix.NetworkGpx}:$networkId"

  override def networks(subset: Subset, stale: Boolean): Seq[NetworkAttributes] = {
    if (mongoEnabled) {
      new MongoQuerySubsetNetworks(mongoDatabase).execute(subset)
    }
    else {
      NetworkView.query(analysisDatabase, subset, stale)
    }
  }
}
