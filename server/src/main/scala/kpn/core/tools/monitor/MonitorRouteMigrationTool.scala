package kpn.core.tools.monitor


import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.DatabaseCollection
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzerImpl
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzerImpl
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.OldMonitorRoute
import kpn.server.monitor.domain.OldMonitorRouteReference
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl
import kpn.server.monitor.route.update.MonitorRouteGapAnalyzer
import kpn.server.monitor.route.update.MonitorRouteRelationRepository
import kpn.server.monitor.route.update.MonitorRouteStructureLoader
import kpn.server.monitor.route.update.MonitorRouteUpdateExecutor
import kpn.server.monitor.route.update.MonitorUpdateContext
import kpn.server.monitor.route.update.MonitorUpdateReporterLogger
import kpn.server.monitor.route.update.MonitorUpdateStructureImpl
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.GeometryFactory
import org.mongodb.scala.MongoNamespace

import java.io.File
import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class MonitorExampleSuperRoute(relationId: Long, relations: Seq[MonitorExampleSuperRouteSubRelation])

case class MonitorExampleSuperRouteSubRelation(relationId: Long, referenceFilename: String)

class MonitorRouteMigrationConfiguration(val database: Database) {

  val monitorGroupRepository = new MonitorGroupRepositoryImpl(database)
  val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)
  private val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
  private val monitorRouteRelationRepository = new MonitorRouteRelationRepository(overpassQueryExecutor)
  private val monitorRouteStructureLoader = new MonitorRouteStructureLoader(overpassQueryExecutor)
  private val monitorUpdateStructure = new MonitorUpdateStructureImpl(monitorRouteRelationRepository, monitorRouteStructureLoader)
  private val monitorRouteOsmSegmentAnalyzer = new MonitorRouteOsmSegmentAnalyzerImpl()
  private val monitorRouteDeviationAnalyzer = new MonitorRouteDeviationAnalyzerImpl()
  private val monitorRouteGapAnalyzer = new MonitorRouteGapAnalyzer()

  val monitorRouteUpdateExecutor = new MonitorRouteUpdateExecutor(
    monitorGroupRepository,
    monitorRouteRepository,
    monitorUpdateStructure,
    monitorRouteRelationRepository,
    monitorRouteOsmSegmentAnalyzer,
    monitorRouteGapAnalyzer,
    monitorRouteDeviationAnalyzer
  )
}

object MonitorRouteMigrationTool {

  private val databaseName = "kpn-monitor"

  private val exampleSuperRoute = MonitorExampleSuperRoute(
    9453563L,
    Seq(
      MonitorExampleSuperRouteSubRelation(6691445L, "/kpn/test/578-groene-hartpad-etappe-01.gpx"),
      MonitorExampleSuperRouteSubRelation(6769053L, "/kpn/test/579-groene-hartpad-etappe-02.gpx"),
      MonitorExampleSuperRouteSubRelation(9510415L, "/kpn/test/580-groene-hartpad-etappe-03.gpx"),
      MonitorExampleSuperRouteSubRelation(6691488L, "/kpn/test/581-groene-hartpad-etappe-04.gpx"),
      MonitorExampleSuperRouteSubRelation(6691494L, "/kpn/test/582-groene-hartpad-etappe-05.gpx"),
      MonitorExampleSuperRouteSubRelation(9510414L, "/kpn/test/583-groene-hartpad-etappe-06.gpx"),
      MonitorExampleSuperRouteSubRelation(6691523L, "/kpn/test/584-groene-hartpad-etappe-07.gpx"),
      MonitorExampleSuperRouteSubRelation(7217854L, "/kpn/test/585-groene-hartpad-etappe-08.gpx"),
      MonitorExampleSuperRouteSubRelation(9510413L, "/kpn/test/586-groene-hartpad-etappe-09.gpx"),
      MonitorExampleSuperRouteSubRelation(6785059L, "/kpn/test/587-groene-hartpad-etappe-10.gpx"),
      MonitorExampleSuperRouteSubRelation(6691446L, "/kpn/test/588-groene-hartpad-etappe-11.gpx"),
    )
  )

  def main(args: Array[String]): Unit = {
    Mongo.executeIn(databaseName) { database =>
      val configuration = new MonitorRouteMigrationConfiguration(database)
      val tool = new MonitorRouteMigrationTool(configuration)
      // tool.renameRouteCollections()
      tool.addExampleSuperRoute(exampleSuperRoute)
      // tool.migrateOne("GRV", "p04")
      // tool.migrate()
    }
    println("Done")
  }
}

class MonitorRouteMigrationTool(configuration: MonitorRouteMigrationConfiguration) {

  private val log = Log(classOf[MonitorRouteMigrationTool])
  private val geometryFactory = new GeometryFactory

  def renameRouteCollections(): Unit = {
    renameRouteCollection(configuration.database.monitorRoutes)
    renameRouteCollection(configuration.database.monitorRouteReferences)
    renameRouteCollection(configuration.database.monitorRouteStates)
  }

  def addExampleSuperRoute(exampleSuperRoute: MonitorExampleSuperRoute): Unit = {

    configuration.monitorGroupRepository.groupByName("AAA") match {
      case None => log.error("group not found")
      case Some(group) =>
        configuration.monitorRouteRepository.routeByName(group._id, "example") match {
          case Some(route) => configuration.monitorRouteRepository.deleteRoute(route._id)
          case None =>
        }

        configuration.monitorRouteUpdateExecutor.execute(
          MonitorUpdateContext(
            "migration",
            new MonitorUpdateReporterLogger(),
            MonitorRouteUpdate(
              action = "add",
              groupName = group.name,
              routeName = "example",
              referenceType = "multi-gpx",
              description = Some("example route with gpx trace per sub relation"),
              comment = Some("comment"),
              relationId = Some(exampleSuperRoute.relationId),
            )
          )
        )

        exampleSuperRoute.relations.foreach { superRouteSubRelation =>
          val referenceGpx = FileUtils.readFileToString(new File(superRouteSubRelation.referenceFilename), "UTF-8")
          configuration.monitorRouteUpdateExecutor.execute(
            MonitorUpdateContext(
              "migration",
              new MonitorUpdateReporterLogger(),
              MonitorRouteUpdate(
                action = "gpx-upload",
                groupName = group.name,
                routeName = "example",
                referenceType = "multi-gpx",
                relationId = Some(exampleSuperRoute.relationId),
                referenceTimestamp = Some(Time.now),
                referenceFilename = Some(superRouteSubRelation.referenceFilename),
                referenceGpx = Some(referenceGpx),
              )
            )
          )
        }
    }
  }

  def migrateOne(groupName: String, routeName: String): Unit = {
    configuration.monitorGroupRepository.groupByName(groupName) match {
      case None => log.error("group not found")
      case Some(group) =>
        configuration.monitorRouteRepository.oldRouteByName(group._id, routeName) match {
          case None => log.error("route not found")
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
    Log.context(s"${group.name}:${route.name}") {
      log.infoElapsed {
        val oldReference = configuration.monitorRouteRepository.oldRouteReferenceRouteWithId(route._id)
        migrateRoute(group, route, oldReference)
        ("migrated", ())
      }
    }
  }

  private def migrateRoute(group: MonitorGroup, oldRoute: OldMonitorRoute, oldReference: Option[OldMonitorRouteReference]): Unit = {

    configuration.monitorRouteRepository.routeByName(group._id, oldRoute.name) match {
      case Some(route) => configuration.monitorRouteRepository.deleteRoute(route._id)
      case None =>
    }

    val user = oldReference match {
      case Some(reference) => reference.user
      case None => "migration"
    }

    val migrationGeojson = if (oldRoute.referenceFilename.isDefined) {
      oldReference match {
        case Some(reference) => Some(reference.geometry)
        case None => None
      }
    }
    else {
      None
    }

    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        user,
        new MonitorUpdateReporterLogger(),
        MonitorRouteUpdate(
          action = "add",
          groupName = group.name,
          routeName = oldRoute.name,
          referenceType = oldRoute.referenceType.get,
          description = Some(oldRoute.description),
          comment = oldRoute.comment,
          relationId = oldRoute.relationId,
          referenceTimestamp = oldRoute.referenceDay.map(day => Timestamp(day)),
          referenceFilename = oldRoute.referenceFilename,
          migrationGeojson = migrationGeojson,
        )
      )
    )
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
