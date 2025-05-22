package kpn.core.tools.next.support

import kpn.api.common.Fact
import kpn.api.common.RouteMemberInfo
import kpn.core.doc.Label
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.route.base.analyzers.RouteRoleAnalyzer
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

case class MembersDoc(
  _id: Long,
  members: Seq[RouteMemberInfo],
)

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
      val docs = database.routes.aggregate[MembersDoc](pipeline)

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
