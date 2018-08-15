package kpn.core.load

import kpn.core.db.couch.Couch
import kpn.core.repository.NetworkRepositoryImpl
import kpn.core.repository.RouteRepositoryImpl
import kpn.shared.Subset

/*
  Finds networks contains routes with given refs.
 */

object TempVerifyNetworkRefs {

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

    Couch.executeIn("test2") { database =>

      val repo = new NetworkRepositoryImpl(database)
      val routeRepo = new RouteRepositoryImpl(database)

      val networks = Subset.all.flatMap { subset =>
        repo.networks(subset, Couch.uiTimeout)
      }

      networks.foreach { networkAttributes =>
        println(s"${networkAttributes.country}/${networkAttributes.networkType} ${networkAttributes.name}")
        val network = repo.network(networkAttributes.id, Couch.uiTimeout).get
        val found = network.detail.get.routes.exists { routeInfo =>
          val route = routeRepo.routeWithId(routeInfo.id, Couch.uiTimeout).get

          if (route.tags.has("ref")) {
            println(s"FOUND ${networkAttributes.country}/${networkAttributes.networkType} ${networkAttributes.id} ${networkAttributes.name}")
            true
          }
          else {
            false
          }
        }

        //          rejectedTagValues.exists { case (key, value) =>
        //            val exists = route.analysis.tags.has(key, value)
        //            if (exists) {
        //              println(s"** FOUND ${networkAttributes.country}/${networkAttributes.networkType} ${networkAttributes.id} ${networkAttributes.name} $key=$value")
        //            }
        //            exists
        //          }
        //        }
        //        if (found) {
        //          println(s"FOUND ${networkAttributes.country}/${networkAttributes.networkType} ${networkAttributes.id} ${networkAttributes.name}")
        //        }
      }
    }
  }
}
