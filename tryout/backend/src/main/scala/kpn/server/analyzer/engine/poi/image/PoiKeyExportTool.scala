package kpn.server.analyzer.engine.poi.image

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.tools.config.Dirs
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.StringId
import kpn.database.util.Mongo

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
      val ids = database.pois.aggregate(pipeline, classOf[StringId], log).map(_._id)
      (s"${ids.size} pois", ids)
    }
    val out = new PrintWriter(new FileWriter(s"${Dirs.root}/pois/pois.txt"))
    poiRefStrings.foreach(out.println)
    out.close()
  }
}
