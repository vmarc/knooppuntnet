package kpn.core.tools.next.database

import kpn.core.tools.next.domain.NextRouteRelation
import kpn.core.util.Log
import kpn.server.repository.RouteRepositoryImpl
import org.springframework.stereotype.Component

@Component
class NextRepositoryImpl(database: NextDatabase) extends NextRepository {

  private val log = Log(classOf[RouteRepositoryImpl])

  override def nextRouteRelation(routeId: Long): Option[NextRouteRelation] = {
    database.routeRelations.findById(routeId)
  }
}
