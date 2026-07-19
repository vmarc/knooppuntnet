package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.NetworkRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class RouteNetworkReferencesAnalyzer(networkRepository: NetworkRepository) extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val networkReferences = networkRepository.routeNetworkReferences(context.route._id)
    context.copy(
      _networkReferences = Some(networkReferences)
    )
  }
}
