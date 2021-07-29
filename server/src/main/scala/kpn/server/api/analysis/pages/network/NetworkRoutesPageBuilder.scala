package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkRouteRow
import kpn.api.common.network.NetworkRoutesPage
import kpn.api.custom.Fact
import kpn.core.mongo.Database
import kpn.core.util.Log
import kpn.core.util.NaturalSorting
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkRepository
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.springframework.stereotype.Component

@Component
class NetworkRoutesPageBuilder(
  mongoEnabled: Boolean,
  // old
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository,
  // new
  database: Database
) {

  private val log = Log(classOf[NetworkRoutesPageBuilder])

  def build(networkId: Long): Option[NetworkRoutesPage] = {
    if (networkId == 1) {
      Some(NetworkRoutesPageExample.page)
    }
    else {
      if (mongoEnabled) {
        mongoBuildPage(networkId)
      }
      else {
        oldBuildPage(networkId)
      }
    }
  }

  private def mongoBuildPage(networkId: Long): Option[NetworkRoutesPage] = {
    val pipeline = Seq(
      filter(
        equal("_id", networkId)
      ),
      project(
        fields(
          include("summary"),
          include("routes")
        )
      )
    )
    database.networkInfos.optionAggregate[NetworkRoutesPage](pipeline, log).map { page =>
      page.copy(
        timeInfo = TimeInfoBuilder.timeInfo,
        surveyDateInfo = SurveyDateInfoBuilder.dateInfo,
      )
    }
  }

  // *** old couchdb based code ***

  private def oldBuildPage(networkId: Long): Option[NetworkRoutesPage] = {
    networkRepository.network(networkId).map(oldBuildPageContents)
  }

  private def oldBuildPageContents(networkInfo: NetworkInfo): NetworkRoutesPage = {

    val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)

    val detailRoutes = networkInfo.detail match {
      case Some(detail) => NaturalSorting.sortBy(detail.routes)(_.name)
      case None => Seq.empty
    }

    val routes = detailRoutes.map { route =>
      NetworkRouteRow(
        route.id,
        route.name,
        route.length,
        route.role,
        investigate = route.facts.contains(Fact.RouteBroken),
        accessible = !route.facts.contains(Fact.RouteUnaccessible),
        roleConnection = route.role.contains("connection"),
        lastUpdated = route.lastUpdated,
        lastSurvey = route.lastSurvey,
        route.proposed
      )
    }

    NetworkRoutesPage(
      TimeInfoBuilder.timeInfo,
      SurveyDateInfoBuilder.dateInfo,
      networkInfo.attributes.networkType,
      NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
      routes
    )
  }
}
