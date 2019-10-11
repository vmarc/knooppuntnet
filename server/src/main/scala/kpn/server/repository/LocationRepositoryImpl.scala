package kpn.server.repository

import kpn.core.db.couch.Database
import kpn.core.db.views.LocationDesign
import kpn.core.db.views.LocationView
import kpn.shared.NetworkType
import kpn.shared.common.Ref
import org.springframework.stereotype.Component

@Component
class LocationRepositoryImpl(mainDatabase: Database) extends LocationRepository {

  override def routesWithoutLocation(networkType: NetworkType): Seq[Ref] = {
    val rows = mainDatabase.query(LocationDesign, LocationView)("route-without-location", networkType.newName)
    rows.map(LocationView.toRef).sortBy(_.name)
  }

}
