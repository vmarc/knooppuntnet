package kpn.core.tools.location

import kpn.core.util.Log
import kpn.server.analyzer.engine.AnalyzerEngineImpl
import kpn.server.analyzer.engine.analysis.location.LocationTree
import kpn.server.json.Json
import org.apache.commons.io.FileUtils

import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

object LocationTreePrintTool {

  private val log = Log(classOf[AnalyzerEngineImpl])

  def main(args: Array[String]): Unit = {
    val locations = loadLocations()
    val tree = loadTree()
    printTree(locations, tree.children.get.head)
  }

  private def loadLocations(): Seq[LocationDoc] = {
    log.infoElapsed {
      log.info("Loading locations")
      val filename = "/kpn/locations/fr/locations.json"
      val string = FileUtils.readFileToString(new File(filename), "UTF-8")
      val locationDocs = Json.objectMapper.readValue(string, classOf[LocationDocs])
      val locs = locationDocs.locations
      (s"Loaded ${locs.size} locations", locs)
    }
  }

  private def loadTree(): LocationTree = {
    val filename = "/kpn/locations/fr/tree.json"
    val string = FileUtils.readFileToString(new File(filename), "UTF-8")
    Json.objectMapper.readValue(string, classOf[LocationTree])
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
      tree.childLocations.foreach { child =>
        printTree(locations, child, level + 1)
      }
    }
  }
}
