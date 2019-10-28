package kpn.server.repository

import akka.util.Timeout
import kpn.core.database.Database
import kpn.core.database.doc.GpxDoc
import kpn.core.database.doc.NetworkDoc
import kpn.core.database.views.analyzer.AnalyzerDesign
import kpn.core.database.views.analyzer.NetworkMapView
import kpn.core.database.views.analyzer.NetworkView
import kpn.core.db._
import kpn.core.gpx.GpxFile
import kpn.core.util.Log
import kpn.shared.Subset
import kpn.shared.network.NetworkAttributes
import kpn.shared.network.NetworkInfo
import kpn.shared.network.NetworkMapInfo
import org.springframework.stereotype.Component

@Component
class NetworkRepositoryImpl(analysisDatabase: Database) extends NetworkRepository {

  private val log = Log(classOf[NetworkRepository])

  override def network(networkId: Long, timeout: Timeout): Option[NetworkInfo] = {
    analysisDatabase.docWithId(networkKey(networkId), classOf[NetworkDoc]).map(_.network)
  }

  override def save(network: NetworkInfo): Boolean = {

    val key = networkKey(network.id)

    analysisDatabase.docWithId(key, classOf[NetworkDoc]) match {
      case Some(doc) =>
        if (network == doc.network) {
          log.info(s"""Network "${network.id}" not saved (no change)""")
          false
        }
        else {
          log.infoElapsed(s"""Network "${network.id}" update""") {
            analysisDatabase.save(NetworkDoc(key, network, doc._rev))
            true
          }
        }

      case None =>
        log.infoElapsed(s"""Network "${network.id}" saved""") {
          analysisDatabase.save(NetworkDoc(key, network))
          true
        }
    }
  }

  override def delete(networkId: Long): Unit = {
    analysisDatabase.deleteDocWithId(networkKey(networkId))
  }

  private def networkKey(networkId: Long): String = s"${KeyPrefix.Network}:$networkId"

  override def gpx(networkId: Long, timeout: Timeout): Option[GpxFile] = {
    analysisDatabase.docWithId(gpxKey(networkId), classOf[GpxDoc]).map(_.file)
  }

  override def saveGpxFile(gpxFile: GpxFile): Boolean = {
    val key = gpxKey(gpxFile.networkId)

    def doSave(): Unit = {
      log.info(s"""Save gpx file "${gpxFile.networkId}"""")
      analysisDatabase.save(GpxDoc(key, gpxFile))
    }

    analysisDatabase.docWithId(key, classOf[GpxDoc]) match {
      case Some(doc) =>
        if (gpxFile == doc.file) {
          log.info(s"""Network "${gpxFile.networkId}" gpx not saved (no change)""")
          false
        }
        else {
          log.infoElapsed(s"""Network "${gpxFile.networkId}" gpx update""") {
            analysisDatabase.deleteDocWithId(key)
            doSave()
            true
          }
        }

      case None =>
        log.infoElapsed(s"""Network "${gpxFile.networkId}" gpx saved""") {
          doSave()
          true
        }
    }
  }

  private def gpxKey(networkId: Long): String = s"${KeyPrefix.NetworkGpx}:$networkId"

  override def networks(subset: Subset, timeout: Timeout, stale: Boolean): Seq[NetworkAttributes] = {
    analysisDatabase.old.query(AnalyzerDesign, NetworkView, timeout, stale)(subset.country.domain, subset.networkType.name).map(NetworkView.convert)
  }

  override def networksMap(country: String, networkType: String, timeout: Timeout, stale: Boolean): Seq[NetworkMapInfo] = {
    analysisDatabase.old.query(AnalyzerDesign, NetworkMapView, timeout, stale)(country, networkType).map(NetworkMapView.convert)
  }
}
