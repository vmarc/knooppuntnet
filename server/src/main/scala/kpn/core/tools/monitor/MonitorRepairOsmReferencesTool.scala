package kpn.core.tools.monitor

import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import kpn.server.api.monitor.route.MonitorRouteRelationRepository
import kpn.server.repository.MonitorGroupRepositoryImpl
import kpn.server.repository.MonitorRouteRepositoryImpl
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonWriter

object MonitorRepairOsmReferencesTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new MonitorRepairOsmReferencesTool(database).repair()
    }
  }
}

class MonitorRepairOsmReferencesTool(database: Database) {

  private val groupRepository = new MonitorGroupRepositoryImpl(database)
  private val routeRepository = new MonitorRouteRepositoryImpl(database)
  private val relationRepository = new MonitorRouteRelationRepository(new OverpassQueryExecutorRemoteImpl())

  def repair(): Unit = {

    groupRepository.groups().sortBy(_.name).foreach { group =>
      groupRepository.groupRoutes(group._id).sortBy(_.name).foreach { route =>
        println(s"${group.name}:${route.name} ${route.referenceType}")
        if (route.referenceType.contains("osm")) {
          route.relationId match {
            case None =>
            case Some(relationId) =>
              routeRepository.routeReferenceRouteWithId(route._id) match {
                case None =>
                case Some(reference) =>
                  relationRepository.load(Some(Timestamp(reference.referenceDay.get)), relationId) match {
                    case None =>
                    case Some(relation) =>
                      val routeSegments = MonitorRouteAnalysisSupport.toRouteSegments(relation)
                      val geomFactory = new GeometryFactory
                      val geometryCollection = new GeometryCollection(routeSegments.map(_.lineString).toArray, geomFactory)
                      val geoJsonWriter = new GeoJsonWriter()
                      geoJsonWriter.setEncodeCRS(false)
                      val geometry = geoJsonWriter.write(geometryCollection)
                      val newReference = reference.copy(
                        geometry = geometry
                      )
                      routeRepository.saveRouteReference(newReference)
                      println("   REFERENCE UPDATED")
                  }
              }
          }
        }
      }
    }
  }
}
