package kpn.core.mongo

import kpn.api.common.common.Ref
import kpn.core.database.doc.NetworkDoc
import kpn.core.database.doc.RouteDoc
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

class MongoQueryNode(database: Database) {

  private val log = Log(classOf[MongoQueryNode])

  def findNetworkReferences(nodeId: Long): Seq[Ref] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("network.active", true),
            equal("network.nodeRefs", nodeId),
          )
        ),
        project(
          fields(
            excludeId(),
            include("network.attributes.id"),
            include("network.attributes.name"),
          )
        )
      )
      val networkDocs = database.networks.aggregate[NetworkDoc](pipeline, log)
      val refs = networkDocs.map { networkDoc =>
        Ref(
          networkDoc.network.attributes.id,
          networkDoc.network.attributes.name
        )
      }
      ("find network references", refs)
    }
  }

  def findRouteReferences(nodeId: Long): Seq[Ref] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("route.active", true),
            equal("route.nodeRefs", nodeId),
          )
        ),
        project(
          fields(
            excludeId(),
            include("route.summary.id"),
            include("route.summary.name"),
          )
        )
      )
      val routeDocs = database.routes.aggregate[RouteDoc](pipeline, log)
      val refs = routeDocs.map { routeDoc =>
        Ref(
          routeDoc.route.summary.id,
          routeDoc.route.summary.name
        )
      }
      ("find route references", refs)
    }
  }
}
