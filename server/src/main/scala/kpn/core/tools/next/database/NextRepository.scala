package kpn.core.tools.next.database

import kpn.core.tools.next.domain.NextRouteRelation
import kpn.core.util.Log
import kpn.server.repository.RouteRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class NextRepository(database: NextDatabase) {

  private val log = Log(classOf[RouteRepository])

  def nextRouteRelation(routeId: Long): Option[NextRouteRelation] = {
    database.routeRelations.findById(routeId)
  }

  def allRouteIds(): Seq[Long] = {
    database.routeRelations.ids()
  }
}
