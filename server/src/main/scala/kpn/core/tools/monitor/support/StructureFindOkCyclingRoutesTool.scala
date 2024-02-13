package kpn.core.tools.monitor.support

import kpn.api.custom.NetworkType
import kpn.core.doc.Label
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.util.Mongo
import org.apache.commons.io.FileUtils
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

import java.io.File
import java.nio.charset.Charset

object StructureFindOkCyclingRoutesTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new StructureFindOkCyclingRoutesTool(database).report()
    }
  }
}

class StructureFindOkCyclingRoutesTool(database: Database) {
  def report(): Unit = {
    println("Collecting route ids")
    val pipeline = Seq(
      filter(
        and(
          equal("labels", Label.active),
          equal("labels", Label.networkType(NetworkType.cycling)),
          BsonDocument("""{"facts": { "$size": 0 }}""")
        )
      ),
      project(
        fields(
          include("_id")
        )
      )
    )
    val ids = database.routes.aggregate[Id](pipeline).map(_._id).sorted
    println(s"${ids.size} routes")
    FileUtils.writeStringToFile(
      new File("/kpn/cycling-ok-routes.txt"),
      ids.map(_.toString).mkString("\n"),
      Charset.forName("UTF-8")
    )
  }
}
