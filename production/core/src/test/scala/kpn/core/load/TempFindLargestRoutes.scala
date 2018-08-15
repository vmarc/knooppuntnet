package kpn.core.load

import kpn.core.db.couch.Couch
import kpn.core.repository.OrphanRepositoryImpl
import kpn.core.repository.RouteRepositoryImpl
import kpn.shared.data.Tag

import scala.io.Source

object TempFindLargestRoutes {

  val rejectedTagValues = Seq(
    ("ref", "1000"), //  1000 Feuer Route
    ("ref", "24"), //  Südhessen-Route 24
    ("ref", "Alt"), // Altmühltal-Radweg
    ("ref", "BK"), //  Bodensee-Königssee-Radweg
    ("ref", "B-U"), // Radfernweg Berlin–Usedom
    ("ref", "BRW"), // Bahnradweg Hessen
    ("ref", "DFR"), // Deutsche Fußball Route NRW
    ("ref", "FL"), //  Flamingoroute
    ("ref", "HH-Rü"), //  Radweg Hamburg-Rügen
    ("ref", "HSB"), // Heidelberg-Schwarzwald-Bodensee-Weg
    ("ref", "KBR"), // Königlich-bayerische Radltour
    ("ref", "LHR"), //  Leine-Heide-Radweg
    ("ref", "NRW"), //  Nordrhein-Westfalen
    ("ref", "REN"), //  Rennsteig-Radwanderweg
    ("ref", "RLP"), // Rheinland-Pfalz
    ("ref", "RRR"), // RadRegionRheinland
    ("ref", "RuB"), // Rund um Berlin
    ("ref", "SA"), //  Schwäbische-Alb-Weg
    ("ref", "Sw"), // Schwarzwald-Radweg
    ("ref", "VR"), // VeloRoute Rhein/Rhin
    ("ref", "WBR"), //  Wasserburgenroute
    ("ref", "Weser"), // Weserradweg
    ("ref", "WL"), //  BahnRadRoute Weser - Lippe
    ("ref", "WOR") //  West-Ost-Radweg
  )


  def main(args: Array[String]): Unit = {


    val prefix1 = "2016-01-02 16:27:32.592 INFO  ForkJoinPool-2-worker-1  [2015-12-20 00:00:00, de-rcn, route="
    val prefix2= "2016-01-02 16:27:32.592 INFO  ForkJoinPool-2-worker-1  [2015-12-20 00:00:00, de-rcn, route=2269215] OrphanRoutesLoader orphan route analysis ("

    val lines = Source.fromFile("/home/marcv/tmp/logs/routes.log").getLines().toSeq
    println("lines.size=" + lines.size)

    val routes = lines.map { line =>
      val routeId = line.substring(prefix1.length, prefix1.length + 7).toLong
      val millis = line.substring(prefix2.length).dropRight("ms)".length).toLong
      (routeId, millis)
    }

    val largestRoutes = routes.sortBy(_._2).reverse.filter{case (routeId, millis) => millis > 500}

    Couch.executeIn("test2") { database =>
      val repo = new RouteRepositoryImpl(database)

      largestRoutes.foreach { case(routeId, millis) =>
        repo.routeWithId(routeId, Couch.uiTimeout) match {
          case None => println("route not found: " + routeId)
          case Some(route) =>

            val exclude = rejectedTagValues.exists{case(key, value) => route.tags.has(key, value)}

            if (!exclude) {

              val ref = route.tags("ref") match {
                case Some(value) => value
                case None => "---"
              }
              val name = route.tags("name") match {
                case Some(value) => value
                case None => "---"
              }

              println(s"""("ref", "$ref"), // $name""")

              println(s"${route.summary.country}/${route.summary.networkType} ${route.summary.name} ${millis}ms")
              route.tags.tags.foreach { case(Tag(key, value)) =>
                println(s"    $key=$value")
              }

              println("")
            }
        }
      }
    }

    ()
  }
}
