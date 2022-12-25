package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteRelation
import org.springframework.stereotype.Component

@Component
class MonitorUpdateStructureImpl(
  monitorRouteRelationRepository: MonitorRouteRelationRepository
) extends MonitorUpdateStructure {

  def update(context: MonitorUpdateContext): MonitorUpdateContext = {

    context.newRoute match {
      case None => context
      case Some(newRoute) =>
        newRoute.relationId match {
          case None => context
          case Some(relationId) =>
            monitorRouteRelationRepository.loadStructure(None, relationId) match {
              case None => context // TODO add message in saveResult
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
  }
}
