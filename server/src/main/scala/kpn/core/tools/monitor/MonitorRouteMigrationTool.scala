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

case class MonitorExampleMultiGpxRoute(
  relationId: Long,
  groupName: String,
  routeName: String,
  description: String,
  relations: Seq[MonitorExampleMultiGpxRouteSubRelation]
)

case class MonitorExampleMultiGpxRouteSubRelation(
  relationId: Long,
  referenceFilename: String
)

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

  private val databaseName = "kpn-monitor-2"
  private val log = Log(classOf[MonitorRouteMigrationTool])

  private val groeneHartpad = MonitorExampleMultiGpxRoute(
    9453563L,
    "NL-LAW",
    "_SP12",
    "Groene Hartpad",
    Seq(
      MonitorExampleMultiGpxRouteSubRelation(6691445L, "584-groene-hartpad-etappe-07.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(6769053L, "585-groene-hartpad-etappe-08.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9510415L, "586-groene-hartpad-etappe-09.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(6691488L, "587-groene-hartpad-etappe-10.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(6691494L, "588-groene-hartpad-etappe-11.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9510414L, "578-groene-hartpad-etappe-01.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(6691523L, "579-groene-hartpad-etappe-02.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(7217854L, "580-groene-hartpad-etappe-03.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9510413L, "581-groene-hartpad-etappe-04.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(6785059L, "582-groene-hartpad-etappe-05.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(6691446L, "583-groene-hartpad-etappe-06.gpx"),
    )
  )

  private val kustpad1 = MonitorExampleMultiGpxRoute(
    1959162L,
    "NL-LAW",
    "_LAW-5-1",
    "Kustpad deel 1",
    Seq(
      MonitorExampleMultiGpxRouteSubRelation(9174496L, "251-nederlands-kustpad-1-etappe-01.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9174495L, "252-nederlands-kustpad-1-etappe-02.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9175900L, "253-nederlands-kustpad-1-etappe-03.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9175899L, "254-nederlands-kustpad-1-etappe-04.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9175897L, "255-nederlands-kustpad-1-etappe-05.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9175896L, "256-nederlands-kustpad-1-etappe-06.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9175895L, "257-nederlands-kustpad-1-etappe-07.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9176032L, "258-nederlands-kustpad-1-etappe-08.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9176031L, "259-nederlands-kustpad-1-etappe-09.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9176030L, "260-nederlands-kustpad-1-etappe-10.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9176029L, "261-nederlands-kustpad-1-etappe-11.gpx"),
      // MonitorExampleSuperRouteSubRelation(0L, "829-nederlands-kustpad-1-etappe-12.gpx"),
      // MonitorExampleSuperRouteSubRelation(0L, "263-nederlands-kustpad-1-etappe-13.gpx"),
      // MonitorExampleSuperRouteSubRelation(0L, "262-nederlands-kustpad-1-etappe-14.gpx"),
      // MonitorExampleSuperRouteSubRelation(0L, "264-nederlands-kustpad-1-etappe-15.gpx"),
    )
  )

  private val kustpad2 = MonitorExampleMultiGpxRoute(
    9237672L,
    "NL-LAW",
    "_LAW-5-2",
    "Kustpad deel 2",
    Seq(
      MonitorExampleMultiGpxRouteSubRelation(9231582L, "999-nederlands-kustpad-2-etappe-01.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9231581L, "998-nederlands-kustpad-2-etappe-02.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9231580L, "997-nederlands-kustpad-2-etappe-03.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9231579L, "996-nederlands-kustpad-2-etappe-04.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9231578L, "995-nederlands-kustpad-2-etappe-05.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9235618L, "994-nederlands-kustpad-2-etappe-06.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9235616L, "993-nederlands-kustpad-2-etappe-07.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9235617L, "992-nederlands-kustpad-2-etappe-08.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9235615L, "991-nederlands-kustpad-2-etappe-09.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9235614L, "990-nederlands-kustpad-2-etappe-10.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9235613L, "989-nederlands-kustpad-2-etappe-11.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9235602L, "988-nederlands-kustpad-2-etappe-12.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(9235601L, "987-nederlands-kustpad-2-etappe-13.gpx"),
      // MonitorExampleSuperRouteSubRelation(0L, "985-nederlands-kustpad-2-etappe-a.gpx"),
      // MonitorExampleSuperRouteSubRelation(0L, "984-nederlands-kustpad-2-etappe-b.gpx"),
      // MonitorExampleSuperRouteSubRelation(0L, "983-nederlands-kustpad-2-etappe-v.gpx"),
    )
  )

  private val kustpad3 = MonitorExampleMultiGpxRoute(
    4097849L,
    "NL-LAW",
    "_LAW-5-3",
    "Kustpad deel 3",
    Seq(
      MonitorExampleMultiGpxRouteSubRelation(7887202L, "267-nederlands-kustpad-3-etappe-01.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(7886268L, "267-nederlands-kustpad-3-etappe-01.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(7886141L, "268-nederlands-kustpad-3-etappe-02.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(7886089L, "269-nederlands-kustpad-3-etappe-03.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(7884843L, "270-nederlands-kustpad-3-etappe-04.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(7882518L, "271-nederlands-kustpad-3-etappe-05.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(7881427L, "272-nederlands-kustpad-3-etappe-06.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(7853497L, "273-nederlands-kustpad-3-etappe-07.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(7850754L, "274-nederlands-kustpad-3-etappe-08.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(7854187L, "275-nederlands-kustpad-3-etappe-09.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(7854253L, "276-nederlands-kustpad-3-etappe-10.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(7857368L, "277-nederlands-kustpad-3-etappe-11.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(7857526L, "278-nederlands-kustpad-3-etappe-12.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(7857574L, "279-nederlands-kustpad-3-etappe-13.gpx"),
      // 280-nederlands-kustpad-3-etappe-14-waddenvariant.gpx
      // 281-nederlands-kustpad-3-etappe-15-waddenvariant.gpx
    )
  )

  private val trekvogelpad = MonitorExampleMultiGpxRoute(
    532494L,
    "NL-LAW",
    "_LAW-2",
    "Trekvogelpad",
    Seq(
      MonitorExampleMultiGpxRouteSubRelation(8391301L, "426-trekvogelpad-etappe-01.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8431398L, "427-trekvogelpad-etappe-02.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8431397L, "428-trekvogelpad-etappe-03.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(2998109L, "429-trekvogelpad-etappe-04.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8431608L, "430-trekvogelpad-etappe-05.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8431607L, "431-trekvogelpad-etappe-06.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8431606L, "432-trekvogelpad-etappe-07.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(2998106L, "433-trekvogelpad-etappe-08.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(2998108L, "434-trekvogelpad-etappe-09.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8432104L, "435-trekvogelpad-etappe-10.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8432103L, "436-trekvogelpad-etappe-11.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(3845106L, "437-trekvogelpad-etappe-12.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8423249L, "438-trekvogelpad-etappe-13.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8426005L, "439-trekvogelpad-etappe-14.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8426347L, "440-trekvogelpad-etappe-15.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8426467L, "441-trekvogelpad-etappe-16.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(4810143L, "442-trekvogelpad-etappe-17.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8428101L, "443-trekvogelpad-etappe-18.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(3395454L, "444-trekvogelpad-etappe-19.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(3559608L, "445-trekvogelpad-etappe-20.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8429235L, "446-trekvogelpad-etappe-21.gpx"),
      // MonitorExampleSuperRouteSubRelation(0L, "447-trekvogelpad-etappe-22.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8432102L, "448-trekvogelpad-etappe-23.gpx"),
    )
  )

  private val pieterpad1 = MonitorExampleMultiGpxRoute(
    312993L,
    "NL-LAW",
    "_LAW-9-1",
    "Pieterpad deel 1",
    Seq(
      MonitorExampleMultiGpxRouteSubRelation(8831649L, "472-pieterpad-1-etappe-01.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8832176L, "473-pieterpad-1-etappe-02.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8832222L, "474-pieterpad-1-etappe-03.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8832221L, "475-pieterpad-1-etappe-04.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8832220L, "476-pieterpad-1-etappe-05.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8832392L, "477-pieterpad-1-etappe-06.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8832391L, "478-pieterpad-1-etappe-07.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8832709L, "479-pieterpad-1-etappe-08.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8832708L, "480-pieterpad-1-etappe-09.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8832707L, "481-pieterpad-1-etappe-10.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8832706L, "482-pieterpad-1-etappe-11.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8832705L, "483-pieterpad-1-etappe-12.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8832704L, "651-pieterpad-1-etappe-13.gpx"),
    )
  )

  private val pieterpad2 = MonitorExampleMultiGpxRoute(
    156951L,
    "NL-LAW",
    "_LAW-9-2",
    "Pieterpad deel 2",
    Seq(
      MonitorExampleMultiGpxRouteSubRelation(8834446L, "650-pieterpad-2-etappe-01.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8834445L, "649-pieterpad-2-etappe-02.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8835026L, "648-pieterpad-2-etappe-03.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8835025L, "647-pieterpad-2-etappe-04.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8835024L, "646-pieterpad-2-etappe-05.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8835656L, "645-pieterpad-2-etappe-06.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8835655L, "644-pieterpad-2-etappe-07.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8835654L, "643-pieterpad-2-etappe-08.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8835653L, "642-pieterpad-2-etappe-09.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8835652L, "641-pieterpad-2-etappe-10.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8835651L, "640-pieterpad-2-etappe-11.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8835650L, "639-pieterpad-2-etappe-12.gpx"),
      MonitorExampleMultiGpxRouteSubRelation(8835649L, "638-pieterpad-2-etappe-13.gpx"),
    )
  )

  def main(args: Array[String]): Unit = {
    log.infoElapsed {
      Mongo.executeIn(databaseName) { database =>
        val configuration = new MonitorRouteMigrationConfiguration(database)
        val tool = new MonitorRouteMigrationTool(configuration)
        tool.renameRouteCollections()
        tool.addMultiGpxRoute(groeneHartpad)
        tool.addMultiGpxRoute(kustpad1)
        tool.addMultiGpxRoute(kustpad2)
        tool.addMultiGpxRoute(kustpad3)
        tool.addMultiGpxRoute(trekvogelpad)
        tool.addMultiGpxRoute(pieterpad1)
        tool.addMultiGpxRoute(pieterpad2)
        tool.migrate()
        // tool.migrateOne("NL-LAW", "LAW-2")
      }
      ("done", ())
    }
  }
}

class MonitorRouteMigrationTool(configuration: MonitorRouteMigrationConfiguration) {

  import kpn.core.tools.monitor.MonitorRouteMigrationTool.log

  private val geometryFactory = new GeometryFactory

  def renameRouteCollections(): Unit = {
    renameRouteCollection(configuration.database.monitorRoutes)
    renameRouteCollection(configuration.database.monitorRouteReferences)
    renameRouteCollection(configuration.database.monitorRouteStates)
  }

  def addMultiGpxRoute(exampleSuperRoute: MonitorExampleMultiGpxRoute): Unit = {

    Log.context(s"${exampleSuperRoute.groupName}:${exampleSuperRoute.routeName}") {

      configuration.monitorGroupRepository.groupByName(exampleSuperRoute.groupName) match {
        case None => log.error("group not found")
        case Some(group) =>
          configuration.monitorRouteRepository.routeByName(group._id, exampleSuperRoute.routeName) match {
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
                routeName = exampleSuperRoute.routeName,
                referenceType = "multi-gpx",
                description = Some(exampleSuperRoute.description),
                comment = None,
                relationId = Some(exampleSuperRoute.relationId),
              )
            )
          )

          exampleSuperRoute.relations.foreach { superRouteSubRelation =>
            val filename = s"/kpn/test/${superRouteSubRelation.referenceFilename}"
            val referenceGpx = FileUtils.readFileToString(new File(filename), "UTF-8")
            configuration.monitorRouteUpdateExecutor.execute(
              MonitorUpdateContext(
                "migration",
                new MonitorUpdateReporterLogger(),
                MonitorRouteUpdate(
                  action = "gpx-upload",
                  groupName = group.name,
                  routeName = exampleSuperRoute.routeName,
                  referenceType = "multi-gpx",
                  relationId = Some(superRouteSubRelation.relationId),
                  referenceTimestamp = Some(Time.now),
                  referenceFilename = Some(superRouteSubRelation.referenceFilename),
                  referenceGpx = Some(referenceGpx),
                )
              )
            )
          }
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
