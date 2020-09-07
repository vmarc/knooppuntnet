package kpn.core.tools.support

import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.server.repository.RouteRepositoryImpl

object DeactivateRoutesTool {

  private val routeIds = Seq(
    11030L,
    1184087L,
    1217118L,
    124156L,
    1267408L,
    1346093L,
    1563521L,
    1626161L,
    1688409L,
    1728687L,
    1729144L,
    1756485L,
    1781977L,
    1830638L,
    1830646L,
    1830647L,
    1832327L,
    1996153L,
    2060345L,
    2108832L,
    2245542L,
    2264904L,
    2310013L,
    2342516L,
    2371626L,
    2619872L,
    2810239L,
    3308445L,
    3338248L,
    3444484L,
    37401L,
    4593591L,
    4760273L,
    5185971L,
    5185972L,
    5405953L,
    5453072L,
    5482033L,
    5504118L,
    5516156L,
    5704862L,
    6242347L,
    6344345L,
    6419518L,
    6448623L,
    7103187L,
    7357372L,
    7769722L,
    7769965L,
    7968369L,
    8704889L,
    953251L
  )

  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "attic-analysis") { database =>
      new DeactivateRoutesTool(database).run(routeIds)
    }
  }
}

class DeactivateRoutesTool(database: Database) {

  private val routeRepository = new RouteRepositoryImpl(database)

  def run(routeIds: Seq[Long]): Unit = {

    println("Start")
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      if ((index + 1) % 10 == 0) {
        println(s"${index + 1}/${routeIds.size}")
      }
      routeRepository.routeWithId(routeId) match {
        case None =>
        case Some(routeInfo) =>
          if (routeInfo.active) {
            routeRepository.save(routeInfo.copy(active = false))
          }
      }
    }
    println("Done")
  }
}
