package kpn.server.api.analysis.pages.location

import kpn.api.common.Bounds
import kpn.api.common.location.Ids
import kpn.api.common.location.LocationNodesParameters
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.common.location.LocationSummary
import kpn.api.common.location.LocationSummaryPage
import kpn.api.custom.LocationKey
import kpn.core.util.Log
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class LocationSummaryPageBuilderImpl(
  locationRepository: LocationRepository
) extends LocationSummaryPageBuilder {

  private val log = Log(classOf[LocationSummaryPageBuilderImpl])

  private val nodeBatchSize = 200
  private val routeBatchSize = 50

  override def build(locationKey: LocationKey): Option[LocationSummaryPage] = {

    val factCount = locationRepository.factCount(locationKey.networkType, locationKey.name)
    val nodes = locationRepository.nodes(locationKey, LocationNodesParameters(99999, 0))
    val routes = locationRepository.routes(locationKey, LocationRoutesParameters(99999, 0))

    val bounds = Bounds.from(nodes)

    val nodeIds = nodes.map(_.id)
    val routeIds = routes.map(_.id)


    val actualNodeBatchSize =  {
      val xx = Math.round(nodeIds.size.toDouble / nodeBatchSize)
      val nodeBatchCount = if ((nodeIds.size.toDouble % nodeBatchSize) > 0) {
        xx + 1
      }
      else {
        xx
      }
      if (nodeBatchCount > 0) {
        Math.round(nodeIds.size / nodeBatchCount)
      }
      else {
        nodeBatchSize
      }
    }

    val actualRouteBatchSize = {
      val xx = Math.round(routeIds.size.toDouble / routeBatchSize)
      val routeBatchCount = if ((routeIds.size.toDouble % routeBatchSize) > 0) {
        xx + 1
      }
      else {
        xx
      }
      if (routeBatchCount > 0) {
        Math.round(routeIds.size / routeBatchCount)
      }
      else {
        routeBatchSize
      }
    }

    log.info(s"nodeIds.size=${nodeIds.size}, actualNodeBatchSize=$actualNodeBatchSize")
    log.info(s"routeIds.size=${routeIds.size}, actualRouteBatchSize=$actualRouteBatchSize")

    val nodeBatches = nodeIds.sliding(actualNodeBatchSize, actualNodeBatchSize).map(Ids).toSeq
    val routeBatches = routeIds.sliding(actualRouteBatchSize, actualRouteBatchSize).map(Ids).toSeq

    val summary = LocationSummary(
      factCount,
      nodes.size,
      routes.size,
      0 // TODO changeCount
    )

    Some(
      LocationSummaryPage(
        TimeInfoBuilder.timeInfo,
        summary,
        bounds,
        nodeBatches,
        routeBatches
      )
    )
  }
}
