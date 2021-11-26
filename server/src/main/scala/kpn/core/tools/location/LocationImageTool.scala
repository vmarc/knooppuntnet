package kpn.core.tools.location

import kpn.core.doc.LocationDoc
import kpn.core.doc.LocationGeometryDoc
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationTree
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.gte
import org.mongodb.scala.model.Filters.lt

import java.awt.Color

object LocationImageTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>
      new LocationImageTool(database).print()
    }
  }
}

class LocationImageTool(database: Database) {

  private val log = Log(classOf[LocationImageTool])

  def print(): Unit = {
    val locations = loadLocations(database)
    val locationGeometries = loadLocationGeometries(database)
    val tree = buildTree(locations)
    val france = tree.children.get.head
    val departments = france.children.get
    val cdcs = departments.flatMap(_.children.toSeq.flatten).filter(_.name.startsWith("fr/2")).distinct
    val departmentCommunes = departments.flatMap(_.children.toSeq.flatten).filter(_.name.startsWith("fr/3"))

    val franceGeometry = database.locationGeometries.findByStringId("fr").get.geometry
    val imageWriter = new GeometryImageWriter(3000, 3000, franceGeometry.getEnvelopeInternal)

    cdcs.foreach { cdc =>
      locationGeometries.get(cdc.name) match {
        case None => log.error(s"Could not find cdc geometry ${cdc.name}")
        case Some(geometry) =>
          imageWriter.fill(geometry.geometry, new Color(255, 255, 0, 36))
          cdc.children.toSeq.flatten.foreach { commune =>
            locationGeometries.get(commune.name) match {
              case None => log.error(s"Could not find cdc commune geometry ${commune.name}")
              case Some(communeGeometry) => imageWriter.draw(communeGeometry.geometry, new Color(0, 0, 0, 24), 1)
            }
          }
          imageWriter.draw(geometry.geometry, new Color(0, 0, 128, 128), 2)
      }
    }

    departmentCommunes.foreach { commune =>
      locationGeometries.get(commune.name) match {
        case None => log.error(s"Could not find department commune geometry ${commune.name}")
        case Some(communeGeometry) =>
          imageWriter.fill(communeGeometry.geometry, new Color(0, 255, 255, 24))
          imageWriter.draw(communeGeometry.geometry, new Color(0, 0, 0, 24), 1)
      }
    }

    departments.foreach { department =>
      locationGeometries.get(department.name) match {
        case Some(geometry) => imageWriter.draw(geometry.geometry, Color.red, 2)
        case None => log.error(s"Could not find department geometry ${department.name}")
      }
    }

    imageWriter.draw(franceGeometry, Color.blue, 1)
    imageWriter.write(s"/kpn/locations/france.png")
  }

  private def loadLocations(database: Database): Seq[LocationDoc] = {
    log.infoElapsed {
      log.info("Loading locations")
      val result = database.locations.find[LocationDoc](
        and(
          gte("_id", "fr"),
          lt("_id", "fz")
        )
      )
      (s"Loaded ${result.size} locations", result)
    }
  }

  private def loadLocationGeometries(database: Database): Map[String, LocationGeometryDoc] = {
    log.infoElapsed {
      log.info("Loading location geometries")
      val result = database.locationGeometries.find[LocationGeometryDoc](
        and(
          gte("_id", "fr"),
          lt("_id", "fz")
        )
      )
      (s"Loaded ${result.size} location geometries", result.map(x => x._id -> x).toMap)
    }
  }

  private def buildTree(locations: Seq[LocationDoc]): LocationTree = {
    new NewLocationTreeBuilder().buildTree(locations)
  }
}
