package kpn.core.tools.monitor

import kpn.api.common.monitor.MonitorRouteProperties
import kpn.core.common.Time
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.database.base.Database
import kpn.database.base.DatabaseCollection
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport.toMeters
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzerImpl
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzerImpl
import kpn.server.analyzer.engine.monitor.MonitorRouteReferenceUtil
import kpn.server.analyzer.engine.monitor.MonitorRouteReferenceUtil.geometryFactory
import kpn.server.api.monitor.domain.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.OldMonitorRoute
import kpn.server.api.monitor.domain.OldMonitorRouteReference
import kpn.server.api.monitor.route.MonitorRouteRelationAnalyzerImpl
import kpn.server.api.monitor.route.MonitorRouteRelationRepository
import kpn.server.api.monitor.route.MonitorUpdateAnalyzerImpl
import kpn.server.api.monitor.route.MonitorUpdateReferenceImpl
import kpn.server.api.monitor.route.MonitorUpdateRouteImpl
import kpn.server.api.monitor.route.MonitorUpdateSaverImpl
import kpn.server.api.monitor.route.MonitorUpdateStructureImpl
import kpn.server.api.monitor.route.MonitorUpdaterImpl
import kpn.server.repository.MonitorGroupRepositoryImpl
import kpn.server.repository.MonitorRouteRepositoryImpl
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.mongodb.scala.MongoNamespace

import java.io.File
import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.xml.Elem
import scala.xml.XML

case class MonitorExampleSuperRoute(relationId: Long, relations: Seq[MonitorExampleSuperRouteRelation])

case class MonitorExampleSuperRouteRelation(relationId: Long, referenceFilename: String)

class MonitorRouteMigrationConfiguration(val database: Database) {

  val monitorGroupRepository = new MonitorGroupRepositoryImpl(database)
  val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)
  private val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
  private val monitorUpdateRoute = new MonitorUpdateRouteImpl(monitorGroupRepository)
  private val monitorRouteRelationRepository = new MonitorRouteRelationRepository(overpassQueryExecutor)
  private val monitorUpdateStructure = new MonitorUpdateStructureImpl(monitorRouteRelationRepository)
  private val monitorRouteOsmSegmentAnalyzer = new MonitorRouteOsmSegmentAnalyzerImpl()
  private val monitorRouteDeviationAnalyzer = new MonitorRouteDeviationAnalyzerImpl()
  private val monitorUpdateReference = new MonitorUpdateReferenceImpl(monitorRouteRelationRepository, monitorRouteOsmSegmentAnalyzer)

  val monitorRouteRelationAnalyzer = new MonitorRouteRelationAnalyzerImpl(
    monitorRouteRelationRepository,
    monitorRouteOsmSegmentAnalyzer,
    monitorRouteDeviationAnalyzer
  )

  private val monitorUpdateAnalyzer = new MonitorUpdateAnalyzerImpl(
    monitorRouteRelationAnalyzer
  )
  private val saver = new MonitorUpdateSaverImpl(monitorRouteRepository)
  val monitorUpdater = new MonitorUpdaterImpl(
    monitorGroupRepository,
    monitorRouteRepository,
    monitorUpdateRoute,
    monitorUpdateStructure,
    monitorUpdateReference,
    monitorUpdateAnalyzer,
    saver,
    monitorRouteRelationAnalyzer
  )
}

object MonitorRouteMigrationTool {

  private val databaseName = "kpn-monitor"

  private val exampleSuperRoute = MonitorExampleSuperRoute(
    9453563L,
    Seq(
      MonitorExampleSuperRouteRelation(6691445L, "/kpn/routes/578-groene-hartpad-etappe-01.gpx"),
      MonitorExampleSuperRouteRelation(6769053L, "/kpn/routes/579-groene-hartpad-etappe-02.gpx"),
      MonitorExampleSuperRouteRelation(9510415L, "/kpn/routes/580-groene-hartpad-etappe-03.gpx"),
      MonitorExampleSuperRouteRelation(6691488L, "/kpn/routes/581-groene-hartpad-etappe-04.gpx"),
      MonitorExampleSuperRouteRelation(6691494L, "/kpn/routes/582-groene-hartpad-etappe-05.gpx"),
      MonitorExampleSuperRouteRelation(9510414L, "/kpn/routes/583-groene-hartpad-etappe-06.gpx"),
      MonitorExampleSuperRouteRelation(6691523L, "/kpn/routes/584-groene-hartpad-etappe-07.gpx"),
      MonitorExampleSuperRouteRelation(7217854L, "/kpn/routes/585-groene-hartpad-etappe-08.gpx"),
      MonitorExampleSuperRouteRelation(9510413L, "/kpn/routes/586-groene-hartpad-etappe-09.gpx"),
      MonitorExampleSuperRouteRelation(6785059L, "/kpn/routes/587-groene-hartpad-etappe-10.gpx"),
      MonitorExampleSuperRouteRelation(6691446L, "/kpn/routes/588-groene-hartpad-etappe-11.gpx"),
    )
  )

  def main(args: Array[String]): Unit = {
    Mongo.executeIn(databaseName) { database =>
      val configuration = new MonitorRouteMigrationConfiguration(database)
      val tool = new MonitorRouteMigrationTool(configuration)
      // tool.renameRouteCollections()
      // tool.addExampleSuperRoute(exampleSuperRoute)
      // tool.migrateOne("fr-iwn-Camino", "Voie-Toulouse")
      tool.migrateOne("fr-iwn-Camino", "Voie-Vezelay")
      // tool.migrate()
    }
    println("Done")
  }
}

class MonitorRouteMigrationTool(configuration: MonitorRouteMigrationConfiguration) {

  private val geometryFactory = new GeometryFactory

  def renameRouteCollections(): Unit = {
    renameRouteCollection(configuration.database.monitorRoutes)
    renameRouteCollection(configuration.database.monitorRouteReferences)
    renameRouteCollection(configuration.database.monitorRouteStates)
  }

  def addExampleSuperRoute(exampleSuperRoute: MonitorExampleSuperRoute): Unit = {

    configuration.monitorGroupRepository.groupByName("AAA") match {
      case None => println("group not found")
      case Some(group) =>
        configuration.monitorRouteRepository.routeByName(group._id, "example") match {
          case Some(route) => configuration.monitorRouteRepository.deleteRoute(route._id)
          case None =>
        }

        val properties = MonitorRouteProperties(
          group.name,
          "example",
          "example route with gpx trace per sub relation",
          Some("comment"),
          Some(exampleSuperRoute.relationId),
          "multi-gpx",
          None,
          None,
          referenceFileChanged = false,
        )
        configuration.monitorUpdater.add("user", group.name, properties)

        exampleSuperRoute.relations.foreach { superRouteRelation =>
          val xml: Elem = XML.loadFile(new File(superRouteRelation.referenceFilename))
          configuration.monitorUpdater.upload(
            user = "user",
            groupName = group.name,
            routeName = properties.name,
            relationId = superRouteRelation.relationId,
            referenceDay = Time.now.toDay,
            filename = superRouteRelation.referenceFilename,
            xml = xml
          )
        }
    }
  }

  def migrateOne(groupName: String, routeName: String): Unit = {
    configuration.monitorGroupRepository.groupByName(groupName) match {
      case None => println("group not found")
      case Some(group) =>
        configuration.monitorRouteRepository.oldRouteByName(group._id, routeName) match {
          case None => println("route not found")
          case Some(route) => migrateGroupRoute(group, route)
        }
    }
  }

  def migrate(): Unit = {
    configuration.monitorGroupRepository.groups().sortBy(_.name).foreach { group =>
      configuration.monitorGroupRepository.oldGroupRoutes(group._id).sortBy(_.name).foreach { route =>
        migrateGroupRoute(group, route)
      }
    }
  }

  private def migrateGroupRoute(group: MonitorGroup, route: OldMonitorRoute): Unit = {
    println(s"${group.name}:${route.name}")
    configuration.monitorRouteRepository.oldRouteReferenceRouteWithId(route._id) match {
      case None => println("Could not read reference")
      case Some(reference) => migrateRoute(group, route, reference)
    }
  }

  private def migrateRoute(group: MonitorGroup, oldRoute: OldMonitorRoute, oldReference: OldMonitorRouteReference): Unit = {

    configuration.monitorRouteRepository.routeByName(group._id, oldRoute.name) match {
      case Some(route) => configuration.monitorRouteRepository.deleteRoute(route._id)
      case None =>
    }

    val properties = MonitorRouteProperties(
      group.name,
      oldRoute.name,
      oldRoute.description,
      oldRoute.comment,
      oldRoute.relationId,
      oldRoute.referenceType.get,
      oldRoute.referenceDay,
      oldRoute.referenceFilename,
      referenceFileChanged = false,
    )

    configuration.monitorUpdater.add(oldReference.user, group.name, properties)

    val newRoute = configuration.monitorRouteRepository.routeByName(group._id, oldRoute.name).get


    if (oldRoute.referenceType.contains("gpx")) {
      val geoJson = oldReference.geometry
      val geometry = new GeoJsonReader().read(geoJson)
      val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(geometry)
      val simplifiedLineStrings = referenceLineStrings.map { lineString =>
        val oldCoordinates = lineString.getCoordinates.toList
        val newCoordinates = MonitorRouteAnalysisSupport.simplifyCoordinates(oldCoordinates)
        if (newCoordinates.length != oldCoordinates.length) {
          geometryFactory.createLineString(newCoordinates.toArray)
        }
        else {
          lineString
        }
      }
      val simplifiedGeometry = geometryFactory.createGeometryCollection(simplifiedLineStrings.toArray)
      val simplifiedGeoJson = MonitorRouteAnalysisSupport.toGeoJson(simplifiedGeometry)

      val distance = Math.round(toMeters(simplifiedGeometry.getLength))

      val reference = MonitorRouteReference(
        oldReference._id,
        routeId = newRoute._id,
        relationId = oldReference.relationId.getOrElse(0),
        timestamp = oldReference.created,
        user = oldReference.user,
        bounds = oldReference.bounds,
        referenceType = oldReference.referenceType,
        referenceDay = oldReference.referenceDay.get,
        distance = distance,
        segmentCount = oldReference.segmentCount,
        filename = oldReference.filename,
        geoJson = simplifiedGeoJson
      )

      configuration.monitorRouteRepository.saveRouteReference(reference)

      configuration.monitorRouteRelationAnalyzer.analyzeReference(newRoute._id, reference) match {
        case Some(state) => configuration.monitorRouteRepository.saveRouteState(state)
        case None =>
      }
    }
  }

  private def renameRouteCollection(collection: DatabaseCollection[_]): Unit = {
    val native = collection.native
    val databaseName = native.namespace.getDatabaseName
    val collectionName = native.namespace.getCollectionName
    val namespace = new MongoNamespace(databaseName, s"old-$collectionName")
    val future = native.renameCollection(namespace).toFuture()
    Await.result(future, Duration(5, TimeUnit.SECONDS))
  }
}
