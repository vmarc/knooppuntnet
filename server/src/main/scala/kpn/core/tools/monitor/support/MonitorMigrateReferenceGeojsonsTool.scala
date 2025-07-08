package kpn.core.tools.monitor.support

import kpn.api.base.ObjectId
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.io.geojson.GeoJsonReader

object MonitorMigrateReferenceGeojsonsTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { database =>
      new MonitorMigrateReferenceGeojsonsTool(database).migrate()
    }
  }
}

class MonitorMigrateReferenceGeojsonsTool(database: Database) {
  private val log = Log(classOf[MonitorMigrateReferenceGeojsonsTool])
  private val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)

  def migrate(): Unit = {
    val routeIds = database.monitorRoutes.objectIds()
    val routeIdsSize = routeIds.size
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      log.info(s"${index + 1}/$routeIdsSize ${routeId.oid}")
      migrateRoute(routeId)
    }
  }

  private def migrateRoute(routeId: ObjectId): Unit = {
    monitorRouteRepository.routeReferences(routeId).foreach { reference =>
      val referenceGeometry = new GeoJsonReader().read(reference.referenceGeoJson.get)
      val referenceLines = toReferenceLines(referenceGeometry)
      val updatedReference = reference.copy(
        referenceLines = referenceLines
      )
      monitorRouteRepository.saveRouteReference(updatedReference)
    }
  }

  private def toReferenceLines(geometry: Geometry): Seq[String] = {
    geometry match {
      case lineString: LineString =>
        Seq(toCoordinates(lineString))
      case geometryCollection: GeometryCollection =>
        toCoordinatesSeq(geometryCollection)
      case _ =>
        throw new IllegalArgumentException(s"Unexpected geometry type: ${geometry.getClass.getSimpleName}")
    }
  }

  private def toCoordinatesSeq(geometryCollection: GeometryCollection): Seq[String] = {
    0.until(geometryCollection.getNumGeometries).map { index =>
      val lineString = geometryCollection.getGeometryN(index).asInstanceOf[LineString]
      toCoordinates(lineString)
    }
  }

  private def toCoordinates(lineString: LineString): String = {
    lineString.getCoordinates.map(c => s"[${c.x},${c.y}]").mkString("[", ",", "]")
  }
}
