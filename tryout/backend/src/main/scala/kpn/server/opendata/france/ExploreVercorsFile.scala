package kpn.server.opendata.france

import kpn.core.util.Haversine
import kpn.core.util.Report
import kpn.server.opendata.france.ExploreVercorsFile.routeDescriptions
import mil.nga.geopackage.GeoPackageManager
import mil.nga.geopackage.features.user.FeatureRow
import mil.nga.sf.GeometryType
import mil.nga.sf.LineString

import java.io.File
import scala.jdk.CollectionConverters.IterableHasAsScala

object ExploreVercorsFile {
  def main(args: Array[String]): Unit = {
    new ExploreVercorsFile().explore()
  }

  val routeDescriptions: Map[String, String] = Seq(
    "best" -> "Balcon Est",
    "cs_gs" -> "chemins du soleil Grenoble Sisteron",
    "cs_vg" -> "chemins du soleil Valence Gap",
    "fb" -> "fil de la Bourne",
    "gr-9" -> "GR 9",
    "grp-tc" -> "GR de Pays Tour des Coulmes",
    "grp-gerv" -> "GR de Pays Gervanne",
    "grp-t4m" -> "GR de pays Tour des 4 Montagnes",
    "grp-tmm" -> "GRP Tour des Monts du Matin",
    "grp-tvd" -> "GR de pays tour du Vercors Drôme",
    "gtvp1" -> "gtvp itinéraire principal",
    "gtvpl" -> "gtvp liaison",
    "gtvpthp" -> "gtvp traversée des hauts Plateaux",
    "gtvpv" -> "variante gtvp (variante hivernale trièves)",
    "gtvvtt1" -> "gtv vtt itinéraire principal",
    "gtvvttl" -> "gtv vtt liaison",
    "gtvvttpjt" -> "projet de gtv vtt",
    "gtvvttthp" -> "gtv VTT traversée des hauts Plateaux",
    "gtvvttv" -> "variante gtv vtt",
    "hr" -> "hors réseau balisé",
    "ptrs" -> "petites routes du soleil (2017)",
    "rl" -> "route de la lavande diois (2017)",
    "via_smv" -> "via corda St Martin",
  ).toMap
}

class ExploreVercorsFile {
  private val report = new Report

  def explore(): Unit = {
    val rows = readRows()
    report.print(s"rowcount: ${rows.size}")
    printRows(rows)
    printRoutesWithoutName(rows)
    printRouteDistances(rows)
    printTotalDistance(rows)
    printRouteSurfaces(rows)
    printRouteSurfaceDistances(rows)
  }

  private def readRows(): Seq[FeatureRow] = {
    val file = new File("/Users/marc/kpn/opendata/france/itineraires-rando-parc-du-vercors.gpkg")
    val geoPackage = GeoPackageManager.open(file)
    val featureTable = geoPackage.getFeatureTables.get(0)
    val featureDao = geoPackage.getFeatureDao(featureTable)
    val featureResultSet = featureDao.query(Array("fid", "geom", "uid", "revet", "typeroute", "numroute", "voiedouce", "iti_nom"))
    try {
      featureResultSet.asScala.toSeq
    } finally {
      featureResultSet.close()
    }
  }

  private def printRows(rows: Seq[FeatureRow]): Unit = {
    rows.foreach(printRow)
  }

  private def printRoutesWithoutName(rows: Seq[FeatureRow]): Unit = {
    report.print("routes without name")
    report.indent {
      val filteredRows = rows.filter(row => FranceUtil.routeNames(row).isEmpty)
      filteredRows.foreach(printRow)
    }
  }

  private def printRow(row: FeatureRow): Unit = {
    val fid = row.getValue("fid")
    val geom = row.getGeometryType
    val uid = row.getValue("uid")
    val revet = row.getValue("revet")
    val typeroute = row.getValue("typeroute")
    val numroute = row.getValue("numroute")
    val voiedouce = row.getValue("voiedouce")
    val itinom = row.getValue("iti_nom")
    report.print(s"$fid, $geom, $uid, $revet, $typeroute, $numroute, $voiedouce, $itinom")
  }

  private def printRouteDistances(rows: Seq[FeatureRow]): Unit = {
    val names = rows.flatMap(FranceUtil.routeNames)
    val resultMap = names.groupBy(identity).map(e => e._1 -> e._2.size).toSeq.sortBy(_._1.toLowerCase())
    val distances = resultMap.map(_._1).map { routeName =>
      val routeRows = rows.filter(row => FranceUtil.routeNames(row).contains(routeName))
      val distance = routeRows.map(routeDistance).sum
      routeName -> distanceString(distance)
    }

    report.print(s"distances")
    report.indent {
      report.print(s"|id|name|distance|")
      report.print(s"|---|---|---|")
      distances.foreach { d =>
        val description = routeDescriptions.getOrElse(d._1, "?")
        report.print(s"|${d._1}|$description|${d._2}|")
      }
    }
    report.print(s"---")
  }

  private def printTotalDistance(rows: Seq[FeatureRow]): Unit = {
    val meters = rows.map(routeDistance).sum
    report.print(s"total distance: ${meters / 1000}")
    report.print(s"---")
  }

  private def printRouteSurfaces(rows: Seq[FeatureRow]): Unit = {
    val names = rows.map(row => s"${row.getValue("revet")}")
    val resultMap = names.groupBy(identity).map(e => e._1 -> e._2.size).toSeq.sortBy(_._1.toLowerCase())
    report.print(s"surfaces")
    report.indent {
      report.print(s"|surface|rowCount|")
      report.print(s"|---|---|")
      resultMap.foreach { d =>
        report.print(s"|${d._1}|${d._2}|")
      }
    }
    report.print(s"---")
  }

  private def printRouteSurfaceDistances(rows: Seq[FeatureRow]): Unit = {
    val surfaces = rows.map(row => s"${row.getValue("revet")}").distinct.sorted
    val surfaceDistances = surfaces.map { surface =>
      val surfaceRows = rows.filter(row => s"${row.getValue("revet")}" == surface)
      val distance = surfaceRows.map(routeDistance).sum
      surface -> distanceString(distance)
    }

    report.print(s"surfaces")
    report.indent {
      report.print(s"|surface|distance|")
      report.print(s"|---|---|")
      surfaceDistances.foreach { d =>
        report.print(s"|${d._1}|${d._2}|")
      }
    }
    report.print(s"---")
  }

  private def routeDistance(row: FeatureRow): Int = {
    val geometry = row.getGeometry.getGeometry
    if (geometry != null && geometry.getGeometryType == GeometryType.LINESTRING) {
      val lineString = geometry.asInstanceOf[LineString]
      val points = lineString.getPoints.asScala.toSeq
      val coordinates = points.map { point =>
        FranceUtil.lambertToLatLon(point.getX, point.getY)
      }
      Haversine.meters(coordinates)
    }
    else {
      0
    }
  }

  private def distanceString(meters: Int): String = {
    if (meters < 1000) {
      s"${meters}m"
    } else {
      s"${meters / 1000}km"
    }
  }
}
