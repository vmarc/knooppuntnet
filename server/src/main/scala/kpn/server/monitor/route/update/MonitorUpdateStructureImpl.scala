package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.MonitorUtil
import org.springframework.stereotype.Component

@Component
class MonitorUpdateStructureImpl(
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteStructureLoader: MonitorRouteStructureLoader
) extends MonitorUpdateStructure {

  private val log = Log(classOf[MonitorUpdateStructureImpl])

  def update(context: MonitorUpdateContext): MonitorUpdateContext = {

    context.newRoute match {
      case None =>
        // no changes to route properties, so no need to pick up route structure again
        context

      case Some(newRoute) =>
        newRoute.relationId match {
          case None =>
            // relationId has not been defined for the route yet, so cannot pick up route structure yet
            val updatedNewRoute = context.newRoute.get.copy(
              symbol = None,
              deviationDistance = 0,
              deviationCount = 0,
              osmWayCount = 0,
              osmDistance = 0,
              osmSegmentCount = 0,
              osmSegments = Seq.empty,
              relation = None,
              happy = false
            )

            val updatedNewRoute2 = if (updatedNewRoute.referenceType == "osm") {
              updatedNewRoute.copy(
                referenceFilename = None,
                referenceDistance = 0,
              )
            }
            else {
              updatedNewRoute
            }

            context.copy(
              newRoute = Some(updatedNewRoute2)
            )

          case Some(relationId) =>

            context.oldRoute match {
              case None =>
                // this is a new route: need to read the route structure
                loadStructure(context, newRoute, relationId)

              case Some(oldRoute) =>
                if (oldRoute.relationId.contains(relationId) && oldRoute.referenceTimestamp == newRoute.referenceTimestamp) {
                  // relationId in the new route is the same as the old one, no need to pick up the structure, we should already know the structure
                  context
                }
                else {
                  loadStructure(context, newRoute, relationId)
                }
            }
        }
    }
  }

  private def loadStructure(context: MonitorUpdateContext, newRoute: MonitorRoute, relationId: Long): MonitorUpdateContext = {
    log.info(s"load structure relationId=$relationId")
    monitorRouteStructureLoader.load(None, relationId) match {
      case None =>
        context // TODO add message in saveResult: "could not load route structure"
      case Some(monitorRouteRelation) =>
        log.info(s"load structure relationId=$relationId, subrelations=${monitorRouteRelation.relations.size}")
        val updatedMonitorRouteRelation = if (newRoute.referenceType == "multi-gpx") {
          updateMultiGpxReferences(monitorRouteRelation, newRoute)
        }
        else {
          monitorRouteRelation
        }

        val updatedRoute = newRoute.copy(
          relation = Some(updatedMonitorRouteRelation)
        )
        context.copy(
          newRoute = Some(updatedRoute),
          structureChanged = true
        )
    }
  }


  private def updateMultiGpxReferences(monitorRouteRelation: MonitorRouteRelation, oldRoute: MonitorRoute): MonitorRouteRelation = {

    val (referenceTimestamp, referenceFilename, referenceDistance) = MonitorUtil.subRelation(oldRoute, monitorRouteRelation.relationId) match {
      case Some(subRelation) => (subRelation.referenceTimestamp, subRelation.referenceFilename, subRelation.referenceDistance)
      case None => (None, None, 0L)
    }

    val updatedRelations = monitorRouteRelation.relations.map(r => updateMultiGpxReferences(r, oldRoute))
    monitorRouteRelation.copy(
      referenceTimestamp = referenceTimestamp,
      referenceFilename = referenceFilename,
      referenceDistance = referenceDistance,
      relations = updatedRelations,
    )
  }
}
