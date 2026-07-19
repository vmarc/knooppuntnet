package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.Fact
import kpn.api.common.data.MemberType
import kpn.core.analysis.Facts
import kpn.core.doc.NetworkRouteDetail
import kpn.core.util.Log
import kpn.core.util.NaturalSorting
import kpn.server.repository.RouteRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class NetworkRouteAnalyzer(routeRepository: RouteRepository) extends NetworkAnalyzer {

  private val log = Log(classOf[NetworkAnalysisContext])

  override def analyze(context: NetworkAnalysisContext): NetworkAnalysisContext = {
    val routeIds = context.network.base.members.filter(_.memberType == MemberType.Relation).map(_.ref)
    val routeDetails = queryRouteDetails(routeIds)
    val meters = routeDetails.map(_.length).sum
    val km = Math.round(meters.toDouble / 1000)
    val enrichedRouteDetails = routeDetails.map { networkRouteDetail =>
      val role = context.network.base.members
        .find(member => member.memberType == MemberType.Relation && member.ref == networkRouteDetail.id)
        .flatMap(_.role)
      val investigate = networkRouteDetail.facts.exists(Facts.isError)
      val accessible = !networkRouteDetail.facts.contains(Fact.RouteInaccessible)
      val roleConnection = role.contains("connection")
      networkRouteDetail.copy(
        role = role,
        investigate = investigate,
        accessible = accessible,
        roleConnection = roleConnection,
      )
    }
    val sortedRouteDetails = NaturalSorting.sortBy(enrichedRouteDetails)(_.name)
    val connectionCount = enrichedRouteDetails.count(_.roleConnection)

    context.copy(
      _routeDetails = Some(sortedRouteDetails),
      _connectionCount = Some(connectionCount),
      _meters = Some(meters),
      _km = Some(km)
    )
  }

  private def queryRouteDetails(routeIds: Seq[Long]): Seq[NetworkRouteDetail] = {
    routeRepository.networkRouteDetails(routeIds)
  }
}
