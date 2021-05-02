package kpn.server.api.analysis.pages.location

import kpn.api.common.Bounds
import kpn.api.common.location.LocationEditPage
import kpn.api.common.location.LocationNodesParameters
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.custom.LocationKey
import kpn.api.custom.LocationNodesType
import kpn.api.custom.LocationRoutesType
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class LocationEditPageBuilderImpl(locationRepository: LocationRepository) extends LocationEditPageBuilder {

  private val maxNodes = 200L

  override def build(locationKey: LocationKey): Option[LocationEditPage] = {

    val summary = locationRepository.summary(locationKey)

    if (summary.nodeCount > maxNodes) {
      Some(
        LocationEditPage(
          TimeInfoBuilder.timeInfo,
          summary,
          tooManyNodes = true,
          maxNodes,
          Bounds(),
          Seq.empty,
          Seq.empty
        )
      )
    }
    else {
      val nodes = locationRepository.nodes(locationKey, LocationNodesParameters(LocationNodesType.all, 99999))
      val routes = locationRepository.routes(locationKey, LocationRoutesParameters(LocationRoutesType.all, 99999))

      val bounds = Bounds.from(nodes, 0.15)

      val nodeIds = nodes.map(_.id)
      val routeIds = routes.map(_.id)

      Some(
        LocationEditPage(
          TimeInfoBuilder.timeInfo,
          summary,
          tooManyNodes = false,
          maxNodes,
          bounds,
          nodeIds,
          routeIds
        )
      )
    }
  }
}
