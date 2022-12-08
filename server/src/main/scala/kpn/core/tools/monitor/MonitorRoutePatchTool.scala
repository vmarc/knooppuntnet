package kpn.core.tools.monitor

import kpn.api.custom.Day
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.repository.MonitorGroupRepositoryImpl
import kpn.server.repository.MonitorRouteRepositoryImpl

object MonitorRoutePatchTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new MonitorRoutePatchTool(database).patch()
    }
  }
}

class MonitorRoutePatchTool(database: Database) {

  private val groupRepository = new MonitorGroupRepositoryImpl(database)
  private val routeRepository = new MonitorRouteRepositoryImpl(database)

  def listFilenames(): Unit = {
    groupRepository.groupByName("SGR").foreach { group =>
      groupRepository.groupRoutes(group._id).sortBy(_.name).foreach { route =>
        println(s"${group.name}:${route.name} " + route.referenceFilename)
      }
    }
  }

  def patch(): Unit = {

    val sgrUpdates = Seq(
      "GR-12" -> Day(2022, 7, 30),
      "GR-12-L1" -> Day(2020, 2, 1),
      "GR-121-E4" -> Day(2022, 10, 9),
      "GR-126" -> Day(2022, 9, 4),
      "GR-126-L1" -> Day(2017, 10, 25),
      "GR-126-L2" -> Day(2022, 10, 6),
      "GR-126-L3" -> Day(2016, 8, 11),
      "GR-126-L4" -> Day(2020, 2, 1),
      "GR-126-V1" -> Day(2020, 6, 14),
      "GR-126-V2" -> Day(2016, 8, 11),
      "GR-129S" -> Day(2022, 7, 31),
      "GR-129S-L2" -> Day(2018, 11, 17),
      "GR-129S-V1" -> Day(2020, 6, 26),
      "GR-129W" -> Day(2022, 9, 12),
      "GR-14-E3" -> Day(2022, 6, 23),
      "GR-14-E3-V3" -> Day(2022, 6, 23),
      "GR-15-E4" -> Day(2022, 7, 19),
      "GR-15-E4-L3" -> Day(2021, 2, 17),
      "GR-15-E4-V1" -> Day(2020, 4, 28),
      "GR-15-E4-V2" -> Day(2019, 3, 3),
      "GR-15-E4-V3" -> Day(2022, 10, 3),
      "GR-16-E7" -> Day(2022, 10, 5),
      "GR-16-E7-L1" -> Day(2021, 3, 18),
      "GR-16-E7-L3" -> Day(2022, 10, 5),
      "GR-16-E7-V3" -> Day(2021, 3, 18),
      "GR-16-E7-V5" -> Day(2021, 3, 18),
      "GR-16-E7-V6" -> Day(2021, 3, 18),
      "GR-16-E7-V7" -> Day(2021, 3, 18),
      "GR-16-E7-V8" -> Day(2021, 3, 18),
      "GR-16-E7-V9" -> Day(2021, 3, 18),
      "GR-17-E1-P1" -> Day(2022, 7, 27),
      "GR-17-E1-P2" -> Day(2022, 7, 12),
      "GR-412-E2" -> Day(2022, 9, 5),
      "GR-412-E2-B1" -> Day(2021, 11, 13),
      "GR-412-E2-B2" -> Day(2021, 9, 29),
      "GR-412-E2-B3" -> Day(2021, 9, 29),
      "GR-412-E2-L1" -> Day(2021, 2, 12),
      "GR-412-E2-a" -> Day(2021, 2, 12),
      "GR-5-E7" -> Day(2022, 10, 7),
      "GR-56-E8" -> Day(2022, 9, 20),
      "GR-56-E8-L1" -> Day(2022, 7, 17),
      "GR-56-E8-L2" -> Day(2022, 7, 17),
      "GR-56-E8-V1" -> Day(2022, 7, 29),
      "GR-56-E8-V2" -> Day(2022, 7, 17),
      "GR-56-E8-V3" -> Day(2022, 7, 17),
      "GR-56-E8-V4" -> Day(2022, 7, 17),
      "GR-56-E8-V5" -> Day(2022, 7, 17),
      "GR-56-E8-V6" -> Day(2022, 7, 17),
      "GR-56-E8-V7" -> Day(2022, 7, 31),
      "GR-57-E9-L1" -> Day(2022, 2, 6),
      "GR-57-E9-P1" -> Day(2022, 2, 22),
      "GR-57-E9-P2" -> Day(2022, 2, 6),
      "GR-57-E9-P3" -> Day(2022, 2, 6),
      "GR-573-E6" -> Day(2022, 9, 15),
      "GR-576-E1" -> Day(2020, 2, 26),
      "GR-579-E2" -> Day(2022, 3, 22),
      "GR-579-E2-L2" -> Day(2022, 3, 20),
      "GRP-123-E2" -> Day(2022, 2, 18),
      "GRP-123-E2-L1" -> Day(2021, 4, 15),
      "GRP-123-E2-V1" -> Day(2021, 4, 15),
      "GRP-125-E3" -> Day(2022, 2, 10),
      "GRP-125-E3-L1" -> Day(2020, 4, 5),
      "GRP-127-E1" -> Day(2022, 4, 28),
      "GRP-127-E1-V1" -> Day(2018, 1, 25),
      "GRP-151-E2" -> Day(2021, 9, 7),
      "GRP-151-E2-V1" -> Day(2021, 9, 7),
      "GRP-151-E2-V4" -> Day(2021, 9, 7),
      "GRP-161-E1" -> Day(2022, 5, 14),
      "GRP-563-E3" -> Day(2022, 10, 7),
      "GRP-571-E7" -> Day(2022, 3, 5),
      "GRP-577-E4" -> Day(2022, 3, 21),
      "GRT-SAT-E3-P1" -> Day(2022, 5, 3),
      "GRT-SAT-E3-P2" -> Day(2022, 5, 3),
    )


    groupRepository.groups().sortBy(_.name).foreach { group =>
      groupRepository.groupRoutes(group._id).sortBy(_.name).foreach { route =>
        println(s"${group.name}:${route.name}")
        routeRepository.routeReferenceRouteWithId(route._id) match {
          case None => println("  reference not found")
          case Some(reference) =>
            if (group.name == "SGR") {
              sgrUpdates.find(_._1 == route.name) match {
                case Some(pair) => updateRoute(route, Some(pair._2))
                case None =>
                  val referenceDay = route.referenceType match {
                    case Some(referenceType) => Some(reference.created.toDay)
                    case None => None
                  }
                  updateRoute(route, referenceDay)
              }
            }
            else {
              updateRoute(route, Some(reference.created.toDay))
            }
        }
      }
    }
  }

  private def updateRoute(route: MonitorRoute, referenceDay: Option[Day]): Unit = {
    val migratedRoute = route.copy(
      referenceDay = referenceDay,
    )
    routeRepository.saveRoute(migratedRoute)
    routeRepository.routeReferenceRouteWithId(route._id) match {
      case None =>
      case Some(reference) =>
        if (reference.referenceDay.isEmpty) {
          routeRepository.saveRouteReference(
            reference.copy(referenceDay = referenceDay)
          )
        }
    }
  }
}
