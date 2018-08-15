package kpn.core.load

import kpn.core.db.couch.Couch
import kpn.core.db.json.JsonFormats.routeDocFormat

object TempInvestigateRouteLoading extends App {

  Couch.executeIn("kpn", "master2") { database =>
    val routeDocIds = Seq(
      "route:7237817",
      "route:6809988",
      "route:6795596",
      "route:6800967",
      "route:1831637",
      "route:6795630",
      "route:7313154",
      "route:1831635",
      "route:6795789",
      "route:6795675",
      "route:6800977",
      "route:6795765",
      "route:6800908",
      "route:6795721",
      "route:6800993",
      "route:7013325",
      "route:7102776",
      "route:6680226",
      "route:6689876",
      "route:6687366",
      "route:6695284",
      "route:6680185",
      "route:7046271",
      "route:6680063",
      "route:6963986"
    )

    val routes = database.objectsWithIds(routeDocIds, Couch.batchTimeout, stale = false).map{jsValue =>
      println("size=" + jsValue.toString().size)
      routeDocFormat.read(jsValue)
    }

    println("routes.size=" + routes.size)
    routes.foreach { routeDoc =>
      println(s"  route=${routeDoc.route.id} members.size=${routeDoc.route.analysis.map(_.members.size)}")
    }

  }
}
