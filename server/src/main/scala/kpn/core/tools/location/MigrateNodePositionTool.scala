package kpn.core.tools.location

import kpn.core.util.Geo
import kpn.database.base.Database
import kpn.database.util.Mongo

object MigrateNodePositionTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-next") { database =>
      new MigrateNodePositionTool(database).migrate()
    }
  }
}

class MigrateNodePositionTool(database: Database) {
  def migrate(): Unit = {
    val nodes = database.nodes.findAll()
    println(s"${nodes.size} nodes")
    nodes.zipWithIndex.foreach { case (nodeDoc, index) =>
      if (index % 100 == 0) {
        println(s"$index/${nodes.size}")
      }
      database.nodes.save(
        nodeDoc.copy(
          position = Some(
            Geo.point(nodeDoc.lon, nodeDoc.lat)
          )
        )
      )
    }
  }
}
