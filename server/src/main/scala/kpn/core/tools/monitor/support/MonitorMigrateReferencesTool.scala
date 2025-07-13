package kpn.core.tools.monitor.support

import kpn.api.base.ObjectId
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl
import kpn.server.monitor.route.update.MonitorStore
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.io.geojson.GeoJsonReader

object MonitorMigrateReferencesTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { database =>
      new MonitorMigrateReferencesTool(database).migrate()
    }
  }
}

class MonitorMigrateReferencesTool(database: Database) {
  private val log = Log(classOf[MonitorMigrateReferencesTool])
  private val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)
  private val monitorStore = new MonitorStore(
    monitorRouteRepository,
  )

  def migrate(): Unit = {
    val routeIds = database.monitorRoutes.objectIds()
    val routeIdsSize = routeIds.size
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      log.info(s"${index + 1}/$routeIdsSize ${routeId.oid}")
      migrateRoute(routeId)
    }
  }

  private def migrateRoute(routeId: ObjectId): Unit = {
    monitorRouteRepository.oldReferences(routeId).foreach { oldReference =>
      val referenceGeometry = new GeoJsonReader().read(oldReference.referenceGeoJson.get)
      val referenceLines = toReferenceLines(referenceGeometry)
      val reference = MonitorReference(
        _id = oldReference._id,
        routeId = oldReference.routeId,
        relationId = oldReference.relationId,
        timestamp = oldReference.timestamp,
        user = oldReference.user,
        referenceBounds = oldReference.referenceBounds,
        referenceType = oldReference.referenceType,
        referenceTimestamp = oldReference.referenceTimestamp,
        referenceDistance = oldReference.referenceDistance,
        referenceSegmentCount = oldReference.referenceSegmentCount,
        referenceFilename = oldReference.referenceFilename,
        referenceLines = referenceLines
      )
      monitorStore.saveReference(reference)
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
