package kpn.server.opendata.france

import kpn.server.opendata.common.OpenDataRoute
import mil.nga.geopackage.GeoPackageManager
import mil.nga.geopackage.features.user.FeatureRow
import mil.nga.sf.GeometryType
import mil.nga.sf.LineString

import java.io.File
import scala.jdk.CollectionConverters.IterableHasAsScala

class FranceRouteParser {
  def read(): Seq[OpenDataRoute] = {
    read(new File("/Users/marc/kpn/opendata/france/itineraires-rando-parc-du-vercors.gpkg"))
  }

  private def read(geopackageFile: File): Seq[OpenDataRoute] = {
    val rows = readRows(geopackageFile)
    val networkRows = rows.filterNot(exclude)
    networkRows.flatMap { row =>
      val fid = row.getValue("fid")
      val geometry = row.getGeometry.getGeometry
      if (geometry != null && geometry.getGeometryType == GeometryType.LINESTRING) {
        val lineString = geometry.asInstanceOf[LineString]
        val points = lineString.getPoints.asScala.toSeq
        val coordinates = points.map { point =>
          FranceUtil.lambertToLatLon(point.getX, point.getY)
        }
        Some(OpenDataRoute(fid.toString, virtual = false, coordinates))
      }
      else {
        None
      }
    }
  }

  private def readRows(geopackageFile: File): Seq[FeatureRow] = {
    val geoPackage = GeoPackageManager.open(geopackageFile)
    val featureTable = geoPackage.getFeatureTables.get(0)
    val featureDao = geoPackage.getFeatureDao(featureTable)
    val featureResultSet = featureDao.query(Array("fid", "iti_nom", "geom"))
    try {
      val rows = featureResultSet.asScala.toSeq
      rows
    } finally {
      featureResultSet.close()
    }
  }

  private def exclude(row: FeatureRow): Boolean = {
    val routeNames = FranceUtil.routeNames(row)
    routeNames == Seq("hr") || routeNames == Seq("via_smv") || routeNames.forall(name => name.startsWith("_pjt_"))
  }
}
