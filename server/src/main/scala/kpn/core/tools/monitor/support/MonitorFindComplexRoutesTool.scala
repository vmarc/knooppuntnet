package kpn.core.tools.monitor.support

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object MonitorFindComplexRoutesTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      val tool = new MonitorFindComplexRoutesTool(database)
      tool.run()
    }
  }
}

case class ComplexRouteRelation(id: Long, name: String)

case class ComplexRoute(groupName: String, routeName: String, relations: Seq[ComplexRouteRelation])

/*
   Finds monitor routes that have relations with both ways and relations.
 */
class MonitorFindComplexRoutesTool(database: Database) {

  val monitorGroupRepository = new MonitorGroupRepositoryImpl(database)
  val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)

  def run(): Unit = {
    val complexRoutes = findComplexRoutes()
    report(complexRoutes)
  }

  private def findComplexRoutes(): Seq[ComplexRoute] = {
    val groups = monitorGroupRepository.groups().sortBy(_.name)
    groups.flatMap { group =>
      monitorGroupRepository.groupRoutes(group._id).sortBy(_.name).flatMap { route =>
        complexRoute(group, route)
      }
    }
  }

  private def report(complexRoutes: Seq[ComplexRoute]): Unit = {
    println("| | route | relations |")
    println("|-|-------|-----------|")
    complexRoutes.zipWithIndex.foreach { case (complexRoute, index) =>
      val groupName = complexRoute.groupName
      val routeName = complexRoute.routeName
      val url = s"https://knooppuntnet.nl/fr/monitor/groups/$groupName/routes/routeName"
      val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
      val name = s"$groupName:$routeName"
      val routeLink = s"[$name]($url)"
      print(s"|${index + 1}|$routeLink|")
      complexRoute.relations.foreach { relation =>
        val id = relation.id
        val link = s"[$id](https://www.openstreetmap.org/relation/$id)"
        val info = s"$link : ${relation.name}"
        print(s"$info<br>")
      }
      println("|")
    }
  }

  private def complexRoute(group: MonitorGroup, route: MonitorRoute): Option[ComplexRoute] = {
    route.relation.flatMap { rootRelation =>
      val relations = complexSubRelations(rootRelation)
      if (relations.nonEmpty) {
        Some(ComplexRoute(group.name, route.name, relations))
      }
      else {
        None
      }
    }
  }

  private def complexSubRelations(monitorRouteRelation: MonitorRouteRelation): Seq[ComplexRouteRelation] = {
    if (monitorRouteRelation.osmDistance > 0 && monitorRouteRelation.relations.nonEmpty) {
      val id = monitorRouteRelation.relationId
      val name = monitorRouteRelation.name
      ComplexRouteRelation(id, name) +: monitorRouteRelation.relations.flatMap(complexSubRelations)
    }
    else {
      monitorRouteRelation.relations.flatMap(complexSubRelations)
    }
  }
}
