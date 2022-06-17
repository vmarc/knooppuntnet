package kpn.server.analyzer.engine.poi.image

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.StringId
import kpn.database.util.Mongo
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

import java.io.FileWriter
import java.io.PrintWriter

object PoiKeyExportTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>
      new PoiKeyExportTool(database).exportPoiKeys()
    }
  }
}

class PoiKeyExportTool(database: Database) {

  private val log = Log(classOf[PoiKeyExportTool])

  def exportPoiKeys(): Unit = {
    val poiRefStrings = log.infoElapsed {
      val pipeline = Seq(
        project(
          fields(
            include("_id")
          )
        )
      )
      val ids = database.pois.aggregate[StringId](pipeline, log).map(_._id)
      (s"${ids.size} pois", ids)
    }
    val out = new PrintWriter(new FileWriter("/kpn/pois/pois.txt"))
    poiRefStrings.foreach(out.println)
    out.close()
  }
}
