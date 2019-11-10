package kpn.server.repository

import kpn.api.common.common.Ref
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.database.views.location.LocationView
import org.springframework.stereotype.Component

@Component
class LocationRepositoryImpl(analysisDatabase: Database) extends LocationRepository {

  override def routesWithoutLocation(networkType: NetworkType): Seq[Ref] = {
    LocationView.query(analysisDatabase, "route-without-location", networkType).sortBy(_.name)
  }

}
