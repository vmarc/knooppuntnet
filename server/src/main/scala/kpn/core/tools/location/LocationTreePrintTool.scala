package kpn.core.tools.location

import kpn.core.doc.LocationDoc
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.AnalyzerEngineImpl
import kpn.server.analyzer.engine.analysis.location.LocationTree
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.gte
import org.mongodb.scala.model.Filters.lt

import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

object LocationTreePrintTool {

  private val log = Log(classOf[AnalyzerEngineImpl])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>
      val locations = loadLocations(database)
      val tree = buildTree(locations)
      printTree(locations, tree.children.get.head)
    }
  }

  private def loadLocations(database: Database): Seq[LocationDoc] = {
    log.infoElapsed {
      log.info("Loading locations")
      val result = database.locations.find[LocationDoc](
        and(
          gte("_id", "fr"),
          lt("_id", "fz")
        )
      )
      (s"Loaded ${result.size} locations", result)
    }
  }

  private def buildTree(locations: Seq[LocationDoc]): LocationTree = {
    log.infoElapsed {
      log.info("Building location tree")
      val tree = new NewLocationTreeBuilder().buildTree(locations)
      (s"trees build", tree)
    }
  }

  private def printTree(locations: Seq[LocationDoc], tree: LocationTree): Unit = {
    val fw = new FileWriter(new File("/kpn/locations/france.md"))
    val out = new PrintWriter(fw)
    try {
      val printer = new LocationTreePrintTool(out)
      printer.printTree(locations, tree)
    }
    finally {
      out.close()
    }
  }
}

class LocationTreePrintTool(out: PrintWriter) {
  def printTree(locations: Seq[LocationDoc], tree: LocationTree, level: Int = 0): Unit = {
    locations.find(_._id == tree.name).foreach { location =>
      val spaces = 1.to(level).map(_ => "  ").mkString
      val childCount = if (tree.children.isDefined) s" (${tree.children.get.size})" else ""
      out.println(s"$spaces - `${location._id}` ${location.name} $childCount")
      tree.children.toSeq.flatten.foreach { child =>
        printTree(locations, child, level + 1)
      }
    }
  }
}
