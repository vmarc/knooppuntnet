package kpn.server.api.analysis.pages.location

import kpn.api.common.Bounds
import kpn.api.common.common.Ref
import kpn.api.common.location.LocationEditPage
import kpn.api.common.location.LocationNodesParameters
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.common.location.LocationSummary
import kpn.api.custom.LocationKey
import kpn.core.util.Log
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class LocationEditPageBuilderImpl(
  locationRepository: LocationRepository
) extends LocationEditPageBuilder {

  private val log = Log(classOf[LocationEditPageBuilderImpl])

  override def build(locationKey: LocationKey): Option[LocationEditPage] = {

    val factCount = locationRepository.factCount(locationKey.networkType, locationKey.name)
    val nodes = locationRepository.nodes(locationKey, LocationNodesParameters(99999, 0))
    val routes = locationRepository.routes(locationKey, LocationRoutesParameters(99999, 0))

    val bounds = Bounds.from(nodes)

    val nodeRefs = nodes.map(node => Ref(node.id, node.name))
    val routeRefs = routes.map(route => Ref(route.id, route.name))

    val summary = LocationSummary(
      factCount,
      nodes.size,
      routes.size,
      0 // TODO changeCount
    )

    Some(
      LocationEditPage(
        TimeInfoBuilder.timeInfo,
        summary,
        bounds,
        nodeRefs,
        routeRefs
      )
    )
  }
}
