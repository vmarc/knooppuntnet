package kpn.core.tools.next.support

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.Fact
import kpn.api.common.RouteMemberInfo
import kpn.core.doc.Label
import kpn.core.doc.Storable
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.route.base.analyzers.RouteRoleAnalyzer

case class MembersDoc(
  _id: Long,
  members: Seq[RouteMemberInfo],
) extends Storable

object RouteExploreTool {
  def main(args: Array[String]): Unit = {

    Mongo.executeIn("kpn-next") { database =>
      val pipeline = Seq(
        filter(
          and(
            equal("active", true),
            equal("labels", Label.fact(Fact.RouteInaccessible)),
          )
        ),
        project(
          fields(
            include("members"),
          )
        )
      )
      val docs = database.routes.aggregate(pipeline, classOf[MembersDoc])

      val docCount = docs.length
      println(s"$docCount routes")
      docs.zipWithIndex.foreach { case (doc, index) =>
        if ((index % 1000) == 0) {
          println(s"$index/$docCount")
        }
        val inaccessibleMembers = doc.members.filter { m =>
          m.way.exists(i => !i.accessible) &&
            m.role.nonEmpty &&
            !RouteRoleAnalyzer.knownRoles.contains(m.role.get) &&
            !RouteRoleAnalyzer.poiRoles.contains(m.role.get) &&
            !m.role.exists(_.startsWith("stop"))
        }
        inaccessibleMembers.foreach { member =>
          println(s"routeId=${doc._id}, ${member.role}")
        }
      }
    }
  }
}
