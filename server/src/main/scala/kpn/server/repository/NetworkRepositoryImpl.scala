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
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.changes.NetworkElements
import org.springframework.stereotype.Component

@Component
class NetworkRepositoryImpl(analysisDatabase: Database) extends NetworkRepository {

  override def allNetworkIds(): Seq[Long] = {
    DocumentView.allNetworkIds(analysisDatabase)
  }

  override def network(networkId: Long): Option[NetworkInfo] = {
    analysisDatabase.docWithId(networkKey(networkId), classOf[NetworkDoc]).map(_.network)
  }

  override def elements(networkId: Long): Option[NetworkElements] = {
    analysisDatabase.docWithId(networkElementsKey(networkId), classOf[NetworkElementsDoc]).map(_.networkElements)
  }

  override def saveElements(networkElements: NetworkElements): Unit = {
    val key = networkElementsKey(networkElements.networkId)
    analysisDatabase.save(NetworkElementsDoc(key, networkElements))
  }

  override def save(network: NetworkInfo): Unit = {
    val key = networkKey(network.id)
    analysisDatabase.save(NetworkDoc(key, network))
  }

  override def delete(networkId: Long): Unit = {
    analysisDatabase.deleteDocWithId(networkKey(networkId))
    analysisDatabase.deleteDocWithId(networkElementsKey(networkId))
  }

  private def networkKey(networkId: Long): String = s"${KeyPrefix.Network}:$networkId"

  private def networkElementsKey(networkId: Long): String = s"${KeyPrefix.NetworkElements}:$networkId"

  override def gpx(networkId: Long): Option[GpxFile] = {
    analysisDatabase.docWithId(gpxKey(networkId), classOf[GpxDoc]).map(_.file)
  }

  override def saveGpxFile(gpxFile: GpxFile): Unit = {
    val key = gpxKey(gpxFile.networkId)
    analysisDatabase.save(GpxDoc(key, gpxFile))
  }

  private def gpxKey(networkId: Long): String = s"${KeyPrefix.NetworkGpx}:$networkId"

  override def networks(subset: Subset, stale: Boolean): Seq[NetworkAttributes] = {
    NetworkView.query(analysisDatabase, subset, stale)
  }

}
