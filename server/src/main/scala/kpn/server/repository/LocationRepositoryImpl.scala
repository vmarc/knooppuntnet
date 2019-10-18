package kpn.server.repository

import kpn.core.db.couch.OldDatabase
import kpn.core.db.views.LocationDesign
import kpn.core.db.views.LocationView
import kpn.shared.NetworkType
import kpn.shared.common.Ref
import org.springframework.stereotype.Component

@Component
class LocationRepositoryImpl(oldAnalysisDatabase: OldDatabase) extends LocationRepository {

  override def routesWithoutLocation(networkType: NetworkType): Seq[Ref] = {
    val rows = oldAnalysisDatabase.query(LocationDesign, LocationView)("route-without-location", networkType.newName)
    rows.map(LocationView.toRef).sortBy(_.name)
  }

}
