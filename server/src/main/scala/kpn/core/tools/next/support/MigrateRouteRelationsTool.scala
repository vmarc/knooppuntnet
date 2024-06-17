package kpn.core.tools.next.support

import kpn.core.tools.next.database.NextDatabase
import kpn.core.tools.next.database.NextDatabaseImpl
import kpn.core.tools.next.domain.NextRouteRelation
import kpn.database.util.Mongo.codecRegistry
import org.mongodb.scala.MongoClient

object MigrateRouteRelationsTool {
  def main(args: Array[String]): Unit = {
    val client = MongoClient("mongodb://localhost:27017")
    try {
      val mongoDatabase = client.getDatabase("kpn-next").withCodecRegistry(codecRegistry)
      val database = new NextDatabaseImpl(mongoDatabase)
      val tool = new MigrateRouteRelationsTool(database)
      tool.migrate()
    } finally {
      client.close()
    }
  }
}

class MigrateRouteRelationsTool(database: NextDatabase) {
  def migrate(): Unit = {
    println("collecting document ids")
    val ids = database.oldRouteRelations.ids()
    println(s"processing ${ids.size} documents")
    ids.zipWithIndex.foreach { case (id, index) =>
      if (((index + 1) % 100) == 0) {
        println(s"${index + 1}/${ids.size}")
      }
      database.oldRouteRelations.findById(id).foreach { oldDocument =>
        val newDocument = NextRouteRelation(
          oldDocument._id,
          oldDocument.relation.raw.version,
          oldDocument.relation.raw.timestamp,
          oldDocument.relation.raw.changeSetId,
          oldDocument.relation.raw.tags,
          oldDocument.relation.members
        )
        database.routeRelations.save(newDocument)
      }
    }
  }
}
