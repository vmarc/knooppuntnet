package kpn.server.opendata.france

import kpn.core.util.Report
import mil.nga.geopackage.features.user.FeatureDao
import mil.nga.geopackage.{GeoPackage, GeoPackageManager}

import java.io.File
import scala.jdk.CollectionConverters.IterableHasAsScala

object ExploreGeopackageFile {
  def main(args: Array[String]): Unit = {
    new ExploreGeopackageFile().explore()
  }
}

class ExploreGeopackageFile {
  private val report = new Report

  def explore(): Unit = {
    explore("itineraires-rando-parc-du-vercors.gpkg")
    explore("signaletique.gpkg")
    explore("boucles-pdipr-cd24.gpkg")
    explore("liaisons-pdipr-cd24.gpkg")
  }

  private def explore(name: String): Unit = {
    val filename = s"/Users/marc/kpn/opendata/france/$name"
    report.print(filename)
    report.indent {
      val file = new File(filename)
      val geoPackage = GeoPackageManager.open(file)
      try {
        reportSpatialReferenceSystems(geoPackage)
        reportTiles(geoPackage)
        reportFeatures(geoPackage)
      }
      finally {
        geoPackage.close()
      }
    }
    report.print("---")
  }

  private def reportSpatialReferenceSystems(geoPackage: GeoPackage): Unit = {
    val srsDao = geoPackage.getSpatialReferenceSystemDao
    val spatialReferenceSystems = srsDao.queryForAll().asScala.toSeq
    if (spatialReferenceSystems.isEmpty) {
      report.print("no spatial Reference Systems")
    }
    else {
      report.print("spatial reference systems")
      report.indent {
        spatialReferenceSystems.foreach { srs =>
          report.print(f"${srs.getSrsId}%-5s ${srs.getSrsName}")
        }
      }
    }
  }

  private def reportTiles(geoPackage: GeoPackage): Unit = {
    val tiles = geoPackage.getTileTables
    if (tiles.isEmpty) {
      report.print("no tiles")
    }
    else {
      report.print("tiles present")
    }
  }

  private def reportFeatures(geoPackage: GeoPackage): Unit = {
    val featureTables = geoPackage.getFeatureTables.asScala.toSeq
    report.print(s"${featureTables.size} feature table(s)")
    report.indent {
      featureTables.foreach { featureTable =>
        val dao = geoPackage.getFeatureDao(featureTable)
        report.print(s"feature table '${dao.getTable.getTableName}'")
        report.indent {
          report.print(s"srsId=${dao.getSrsId}")
          reportColumns(dao)
          reportRowCount(dao)
        }
      }
    }
  }

  private def reportColumns(dao: FeatureDao): Unit = {
    report.print(s"columns")
    report.indent {
      val columns = dao.getTable.getColumnNames.map(n => dao.getTable.getColumn(n))
      columns.foreach { column => report.print(f"${column.getName}%-15s ${column.getDataType.name()}")}
    }
  }

  private def reportRowCount(dao: FeatureDao): Unit = {
    val featureResultSet = dao.query(dao.getTable.getColumnNames.take(1))
    val rows = try {
      featureResultSet.asScala.toSeq
    } finally {
      featureResultSet.close()
    }
    report.print(s"rowcount: ${rows.size}")
  }
}
