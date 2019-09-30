package kpn.core.engine.analysis.location

import kpn.core.db.couch.Database
import kpn.core.db.views.LocationDesign
import kpn.core.db.views.LocationView
import kpn.shared.NetworkType
import kpn.shared.common.Ref

class LocationRepositoryImpl(database: Database) extends LocationRepository {

  override def routesWithoutLocation(networkType: NetworkType): Seq[Ref] = {
    val rows = database.query(LocationDesign, LocationView)("route-without-location", networkType.newName)
    rows.map(LocationView.toRef).sortBy(_.name)
  }

}
