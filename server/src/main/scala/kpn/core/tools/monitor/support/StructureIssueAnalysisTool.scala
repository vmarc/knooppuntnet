package kpn.core.tools.monitor.support

import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.repository.RouteRepositoryImpl
import org.apache.commons.io.FileUtils

import java.io.File
import scala.jdk.CollectionConverters.CollectionHasAsScala

object StructureIssueAnalysisTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new StructureIssueAnalysisTool(database).analyze()
    }
  }
}

class StructureIssueAnalysisTool(database: Database) {

  val routeRepository = new RouteRepositoryImpl(database)

  def analyze(): Unit = {
    val routeIds = FileUtils.readLines(new File("/kpn/cycling-nok-routes.txt")).asScala.map(_.toLong)
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      routeRepository.findById(routeId) match {
        case None => println(s"${index + 1}/${routeIds.size} $routeId not found")
        case Some(route) =>
          val hasIssueLink = route.analysis.members.exists { member =>
            StructureIssueLinks.links.contains(member.linkName)
          }
          println(s"${index + 1}/${routeIds.size} $routeId ${if (hasIssueLink) "ISSUE" else "OK"}")
      }
    }
  }
}
