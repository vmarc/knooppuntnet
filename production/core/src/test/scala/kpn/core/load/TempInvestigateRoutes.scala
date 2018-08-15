package kpn.core.load

import kpn.core.db.couch.Couch
import kpn.core.repository.RouteRepositoryImpl
import spray.json.JsString

object TempInvestigateRoutes extends App {

  Couch.executeIn("master") { database =>

    var routeCount = 0
    var ignoredCount = 0
    var notIgnoredCount = 0

    val routeRepository = new RouteRepositoryImpl(database)
    val routeKeys = database.keys("route:0", "route:9999999999", Couch.batchTimeout)
    routeKeys.zipWithIndex.foreach {
      case (JsString(routeKey), index) =>
        val routeId = routeKey.split(":").last.toLong
        val routeInfo = routeRepository.routeWithId(routeId, Couch.batchTimeout).get
        routeCount = routeCount + 1
        if (routeInfo.tags.has("roundtrip", "yes") && !routeInfo.ignored) {
          println(s"$routeId roundtrip=yes orphan=${routeInfo.orphan}")
          notIgnoredCount = notIgnoredCount + 1
        }
        else {
          ignoredCount = ignoredCount + 1
        }
      case _ =>
    }

    println(s"routeCount=$routeCount, ignoredCount=$ignoredCount, notIgnoredCount=$notIgnoredCount")
  }

  def overview(): Unit = {
    Couch.executeIn("master3") { database =>

      val routeRepository = new RouteRepositoryImpl(database)

      val routeKeys = database.keys("route:0", "route:9999999999", Couch.batchTimeout)
      //error.println("routeKeys.size=" + routeKeys.size)
      routeKeys.zipWithIndex.foreach {
        case (JsString(routeKey), index) =>
          val routeId = routeKey.split(":").last.toLong
          val routeInfo = routeRepository.routeWithId(routeId, Couch.batchTimeout).get
          val refs = routeRepository.routeReferences(routeId, Couch.batchTimeout)
          print(index + 1)
          print("\t")
          print(routeInfo.ignored)
          print("\t")
          print(routeInfo.orphan)
          print("\t")
          print(routeInfo.summary.networkType.name)
          print("\t")
          print(routeInfo.summary.country.map(_.domain).getOrElse("-"))
          print("\t")
          print(routeInfo.version)
          print("\t")
          print(routeInfo.summary.meters)
          print("\t")
          print(routeId)
          print("\t[")
          print(routeInfo.summary.tags.tagString)
          print("]\t{")
          print(
            refs.networkReferences.map(ref => ref.id + "/" + ref.name).mkString(",")
          )
          println("}")
        case _ =>
      }
    }
  }

}
