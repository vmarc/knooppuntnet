package kpn.tools.flanders

import com.mongodb.client.model.Filters.and
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.doc.NetworkInfoDoc
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorLocalImpl
import kpn.core.overpass.QueryNodes
import kpn.core.overpass.QueryRelation
import kpn.core.tools.config.Dirs
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.util.Mongo.database
import org.apache.commons.io.FileUtils
import org.mongodb.scala.MongoClient
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

import java.io.File
import java.io.FileReader
import java.nio.charset.Charset
import java.util.Properties

object ExportTool {
  def main(args: Array[String]): Unit = {
    val properties = new File(Dirs.root, "conf/osm.properties")
    val config = new Properties()
    config.load(new FileReader(properties))
    val url = config.getProperty("prod.mongodb.url")
    val mongoClient = MongoClient(url)
    try {
      val overpassQueryExecutor = new OverpassQueryExecutorLocalImpl()
      val mongoDatabase = database(mongoClient, "kpn-prod")
      new ExportTool(mongoDatabase, overpassQueryExecutor).export()
    }
    finally {
      mongoClient.close()
    }
  }
}

class ExportTool(database: Database, overpassQueryExecutor: OverpassQueryExecutor) {

  private val log = Log(classOf[ExportTool])

  def export(): Unit = {
    exportNetworks()
    exportProvinces()
  }

  private def exportProvinces(): Unit = {
    exportProvince("Antwerpen", "be-1-10000")
    exportProvince("Oost-Vlaanderen", "be-1-40000")
    exportProvince("West-Vlaanderen", "be-1-30000")
    exportProvince("Vlaams-Brabant", "be-1-20001")
    exportProvince("Limburg", "be-1-70000")
  }

  private def exportProvince(name: String, location: String): Unit = {
    exportProvinceNodes(name, location)
    exportProvinceRoutes(name, location)
  }

  private def exportProvinceNodes(name: String, location: String): Unit = {
    val dirname = s"/Users/marc/kpn/export/$name"
    new File(dirname).mkdirs
    val pipeline = Seq(
      filter(
        and(
          equal("labels", "active"),
          equal("labels", "network-type-hiking"),
          equal("labels", s"location-$location"),
        ),
      ),
      project(
        fields(
          include("_id")
        )
      )
    )
    val nodeIds = database.nodes.aggregate[Id](pipeline).map(_._id)
    println(s"$name ${nodeIds.size} nodes")
    val xmlString = overpassQueryExecutor.executeQuery(Some(Timestamp(2024, 5, 8, 20, 0, 0)), QueryNodes("nodes", nodeIds))
    val filename = new File(s"$dirname/nodes.xml")
    FileUtils.writeStringToFile(filename, xmlString, Charset.forName("UTF-8"))
  }

  private def exportProvinceRoutes(name: String, location: String): Unit = {
    val dirname = s"/Users/marc/kpn/export/$name/routes"
    new File(dirname).mkdirs
    val pipeline = Seq(
      filter(
        and(
          equal("labels", "active"),
          equal("labels", "network-type-hiking"),
          equal("labels", s"location-$location"),
        ),
      ),
      project(
        fields(
          include("_id")
        )
      )
    )
    val relationIds = database.routes.aggregate[Id](pipeline).map(_._id)
    relationIds.zipWithIndex.foreach { case (relationId, index) =>
      println(s"$name ${index + 1}/${relationIds.size} relationId=$relationId")
      val xmlString = overpassQueryExecutor.executeQuery(Some(Timestamp(2024, 5, 8, 20, 0, 0)), QueryRelation(relationId))
      val filename = new File(s"$dirname/$relationId.xml")
      FileUtils.writeStringToFile(filename, xmlString, Charset.forName("UTF-8"))
    }
  }

  private def exportNetworks(): Unit = {
    new File(s"/Users/marc/kpn/export/networks/").mkdirs
    val relationIds = findFlandersHikingNetworkIds()
    relationIds.zipWithIndex.foreach { case (relationId, index) =>
      println(s"${index + 1}/${relationIds.size} relationId=$relationId")
      val xmlString = overpassQueryExecutor.executeQuery(Some(Timestamp(2024, 5, 8, 20, 0, 0)), QueryRelation(relationId))
      val filename = new File(s"/Users/marc/kpn/export/networks/$relationId.xml")
      FileUtils.writeStringToFile(filename, xmlString, Charset.forName("UTF-8"))
    }
  }

  private def findFlandersHikingNetworkIds(): Seq[Long] = {
    val pipeline = Seq(
      filter(
        and(
          equal("active", true),
          equal("country", "be"),
          equal("summary.networkType", "hiking"),
        ),
      )
    )
    val docs = database.networkInfos.aggregate[NetworkInfoDoc](pipeline)

    val undeterminedNetworks = docs.filter { doc =>
      val tags = doc.detail.tags
      !isNonFlanders(tags) && !isFlanders(tags)
    }

    if (undeterminedNetworks.nonEmpty) {
      println(s"found ${undeterminedNetworks.size} undetermined networks:")
      undeterminedNetworks.foreach { doc =>
        println(s"id=${doc._id}, country=${doc.country}, networkType=${doc.summary.networkType}, name=${doc.summary.name}")
        doc.detail.tags.tags.foreach { tag =>
          println(s"  ${tag.key}=[${tag.value}]")
        }
      }
    }

    docs.filter { doc =>
      isFlanders(doc.detail.tags)
    }.map(_._id)
  }

  private def isFlanders(tags: Tags): Boolean = {
    tags.has(
      "addr:province",
      "Antwerpen",
      "Limburg",
      "Vlaams-Brabant",
      "Oost-Vlaanderen",
      "West-Vlaanderen"
    ) ||
      tags.has(
        "province",
        "West-Vlaanderen",
        "Antwerpen",
      ) ||
      tags.has(
        "operator",
        "Tourisme Provincie Antwerpen",
        "Toerisme Provincie Antwerpen",
        "Toerisme Provincie Oost-Vlaanderen",
        "Toerisme Oost-Vlaanderen",
        "Toerisme Vlaams-Brabant",
        "Westtoer",
        "Toerisme Provincie Vlaams-Brabant",
        "Regionaal Landschap Lage Kempen"
      ) ||
      tags.has(
        "name",
        "Wandelnetwerk Kust",
        "Wandelnetwerk Ham",
        "Wandelnetwerk Demer en Dijle",
        "Wandelnetwerk Dendervallei Noord",
        "Wandelnetwerk De Wijers",
        "Wandelen in Korspel"
      )
  }

  private def isNonFlanders(tags: Tags): Boolean = {
    //    tags.has(
    //      "addr:province",
    //      "Limburg",
    //      "Vlaams-Brabant",
    //      "Oost-Vlaanderen",
    //      "West-Vlaanderen"
    //    ) ||
    //      tags.has(
    //        "province",
    //        "West-Vlaanderen",
    //        "Antwerpen",
    //      ) ||
    tags.has(
      "operator",
      "Wallonie Picarde",
      "Tourismusagentur Ostbelgien",
      "Province de Liège Tourisme"
    ) ||
      tags.has(
        "name",
        "De Wijers lange-afstands-wandeling",
      )
  }
}
