package kpn.server.repository

import com.mongodb.client.model.Filters.in
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Timestamp
import kpn.core.doc.RawNetworkDoc
import kpn.core.doc.RawNodeDoc
import kpn.core.doc.RawRouteDoc
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.filter

class RawDataRepositoryDevelopmentImpl(
  database: Database
) extends RawDataRepository {

  private val log = Log(classOf[RawDataRepositoryDevelopmentImpl])

  override def nodeIds(timestamp: Timestamp): Seq[Long] = {
    log.info("Collecting node ids")
    log.infoElapsed {
      val ids = database.rawNodes.ids(log)
      (s"Collected ${ids.size} node ids", ids)
    }
  }

  override def nodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[RawNode] = {
    val pipeline = Seq(
      filter(
        in("_id", nodeIds: _*),
      ),
    )
    database.rawNodes.aggregate(pipeline, classOf[RawNodeDoc]).map(_.node)
  }

  override def networkIds(timestamp: Timestamp): Seq[Long] = {
    log.info("Collecting network ids")
    log.infoElapsed {
      val ids = database.rawNetworks.ids(log)
      (s"Collected ${ids.size} network ids", ids)
    }
  }

  override def networks(timestamp: Timestamp, networkIds: Seq[Long]): Seq[RawRelation] = {
    val pipeline = Seq(
      filter(
        in("_id", networkIds: _*),
      ),
    )
    database.rawNetworks.aggregate(pipeline, classOf[RawNetworkDoc]).map(_.relation)
  }

  override def routeIds(timestamp: Timestamp): Seq[Long] = {
    log.info("Collecting route ids")
    log.infoElapsed {
      val ids = database.rawRoutes.ids(log)
      (s"Collected ${ids.size} network ids", ids)
    }
  }

  override def route(timestamp: Timestamp, routeId: Long): Option[RawRouteDoc] = {
    database.rawRoutes.findById(routeId)
  }
}
