package kpn.core.mongo.migration

import kpn.api.common.network.NetworkInfo
import kpn.core.db.couch.Couch
import kpn.core.mongo.Database
import kpn.core.mongo.NodeNetworkRef
import kpn.core.mongo.RouteNetworkRef
import kpn.core.mongo.actions.base.MongoSave
import kpn.core.mongo.migration.MigrateNetworksTool.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.repository.NetworkRepositoryImpl

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MigrateNetworksTool {

  private val log = Log(classOf[MigrateNetworksTool])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { mongoDatabase =>
      Couch.executeIn("kpn-database", "analysis") { couchDatabase =>
        new MigrateNetworksTool(couchDatabase, mongoDatabase).migrate()
      }
    }
    log.info("Done")
  }
}

class MigrateNetworksTool(couchDatabase: kpn.core.database.Database, database: Database) {

  private val networkRepository = new NetworkRepositoryImpl(database, couchDatabase, mongoEnabled = false)

  def migrate(): Unit = {
    val networkIds = findNetworkIds()
    networkIds.zipWithIndex.foreach { case (networkId, index) =>
      log.info(s"${index + 1}/${networkIds.size}")
      networkRepository.network(networkId) match {
        case Some(network) => migrateNetwork(network)
        case None =>
      }

      networkRepository.elements(networkId) match {
        case Some(networkElements) =>
          val migrated = networkElements.copy(_id = networkElements.networkId)
          new MongoSave(database).execute("network-elements", migrated)
        case None =>
      }

      networkRepository.gpx(networkId) match {
        case Some(gpxFile) =>
          val migrated = gpxFile.copy(_id = gpxFile.networkId)
          new MongoSave(database).execute("network-gpxs", migrated)
        case None =>
      }
    }
  }

  private def findNetworkIds(): Seq[Long] = {
    log.info("Collecting networkIds")
    networkRepository.allNetworkIds()
  }

  private def migrateNetwork(networkInfo: NetworkInfo): Unit = {
    val migratedNetworkInfo = networkInfo.copy(_id = networkInfo.id)
    new MongoSave(database).execute("networks", migratedNetworkInfo)
    migrateNodeNetworkRefs(migratedNetworkInfo)
    migrateRouteNetworkRefs(migratedNetworkInfo)
  }

  private def migrateNodeNetworkRefs(networkInfo: NetworkInfo): Unit = {
    val refs = networkInfo.nodeRefs.map { nodeId =>
      val networkType = networkInfo.attributes.networkType
      val _id = s"$nodeId:${networkInfo.id}"
      NodeNetworkRef(
        _id,
        nodeId,
        networkInfo._id,
        networkType,
        networkInfo.attributes.name
      )
    }
    val uniqueRefs = refs.map(ref => ref._id -> ref).toMap.values.toSeq
    if (uniqueRefs.nonEmpty) {
      val future = database.nodeNetworkRefs.tempCollection.insertMany(uniqueRefs).toFuture()
      Await.result(future, Duration(1, TimeUnit.MINUTES))
    }
  }

  private def migrateRouteNetworkRefs(networkInfo: NetworkInfo): Unit = {
    val refs = networkInfo.routeRefs.map { routeId =>
      val networkType = networkInfo.attributes.networkType
      val _id = s"$routeId:${networkInfo.id}"
      RouteNetworkRef(
        _id,
        routeId,
        networkInfo._id,
        networkType,
        networkInfo.attributes.name
      )
    }
    val uniqueRefs = refs.map(ref => ref._id -> ref).toMap.values.toSeq
    if (uniqueRefs.nonEmpty) {
      val future = database.routeNetworkRefs.tempCollection.insertMany(uniqueRefs).toFuture()
      Await.result(future, Duration(1, TimeUnit.MINUTES))
    }
  }
}
