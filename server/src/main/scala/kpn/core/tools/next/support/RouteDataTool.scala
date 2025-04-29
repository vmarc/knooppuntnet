package kpn.core.tools.next.support

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo

object RouteDataTool {
  val essenOkRouteIds: Seq[Long] = Seq(
    13844575L, // 01-02
    3294187L, // 01-08
    13844576L, // 01-36
    3665081L, // 02-92
    13844574L, // 02-11
  )

  val law9: Seq[Long] = Seq(
    7973533L, // LAW-9 super route containing other super routes
    312993L, // Pieterpad deel 1 - Pieterburen-Vorden
    8831649L,
    8832176L,
    8832222L,
    8832221L,
    8832220L,
    8832392L,
    8832391L,
    8832709L,
    8832708L,
    8832707L,
    8832706L,
    8832705L,
    8832704L,
    156951L, // Pieterpad deel 2 - Vorden-Maastricht Pietersberg
    8834446L,
    8834445L,
    8835026L,
    8835025L,
    8835024L,
    8835656L,
    8835655L,
    8835654L,
    8835653L,
    8835652L,
    8835651L,
    8835650L,
    8835649L,
  )

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-next") { database =>
      new RouteDataTool(
        database,
        nodeIds = Seq(7903025495L, 1355128623L),
        routeIds = Seq(13844575L)
      ).execute()
    }
  }
}

class RouteDataTool(
  database: Database,
  nodeIds: Seq[Long] = Seq.empty,
  routeIds: Seq[Long] = Seq.empty,
  networkIds: Seq[Long] = Seq.empty
) {

  private val log = Log(classOf[RouteDataTool])

  def execute(): Unit = {
    database.rawNodes.drop()
    database.rawRoutes.drop()
    database.rawNetworks.drop()

    database.rawNodes.bulkSave(database.allRawNodes.findByIds(nodeIds))
    database.rawRoutes.bulkSave(database.allRawRoutes.findByIds(routeIds))
    database.rawNetworks.bulkSave(database.allRawNetworks.findByIds(networkIds))
  }
}
