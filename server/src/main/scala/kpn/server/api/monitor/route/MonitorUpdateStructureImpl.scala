package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.server.api.monitor.domain.MonitorRoute
import org.springframework.stereotype.Component

@Component
class MonitorUpdateStructureImpl(
  monitorRouteRelationRepository: MonitorRouteRelationRepository
) extends MonitorUpdateStructure {

  def update(context: MonitorUpdateContext): MonitorUpdateContext = {

    context.newRoute match {
      case None =>
        // no changes to route properties, so need to pick up route structure again
        context

      case Some(newRoute) =>
        newRoute.relationId match {
          case None =>
            // relationId has not been defined for the route yet, so cannot pick up route structure yet
            context
          // TODO if the old route contained a relationId, and the new one does not, we have stuff to delete?

          case Some(relationId) =>

            context.oldRoute match {
              case None =>
                // this is a new route: need to read the route structure
                loadStructure(context, newRoute, relationId)

              case Some(oldRoute) =>
                if (oldRoute.relationId.contains(relationId)) {
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
    monitorRouteRelationRepository.loadStructure(None, relationId) match {
      case None => context // TODO add message in saveResult: "could not load route structure"
      case Some(relation) =>
        val monitorRouteRelation = MonitorRouteRelation.from(relation, None)
        val updatedRoute = newRoute.copy(
          relation = Some(monitorRouteRelation)
        )
        context.copy(
          newRoute = Some(updatedRoute)
        )
    }
  }
}
