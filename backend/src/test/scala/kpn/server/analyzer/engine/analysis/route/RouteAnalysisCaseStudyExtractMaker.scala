package kpn.server.analyzer.engine.analysis.route

import java.io.File
import java.io.PrintWriter

import kpn.core.loadOld.OsmDataXmlReader
import kpn.core.loadOld.OsmDataXmlWriter
import kpn.api.common.data.raw.RawData

object RouteAnalysisCaseStudyExtractMaker {
  def main(args: Array[String]): Unit = {
    new RouteAnalysisCaseStudyExtractMaker().run()
    println("Ready")
  }
}

class RouteAnalysisCaseStudyExtractMaker {

  private val routeId = 2655355L

  private val inputFileName = "/tmp/relation-1341164.xml"
  private val outputFileName = "/tmp/2655355.xml"

  def run(): Unit = {

    val rawData = OsmDataXmlReader.read(inputFileName)

    rawData.relations.find(_.id == routeId) match {
      case None => println("route relation not found")
      case Some(relation) =>

        val relationIds = relation.relationMembers.map(_.ref)
        if (relationIds.nonEmpty) {
          throw new IllegalStateException("The logic does not support routerelations that contain other relations")
        }

        val ways = {
          val relationWayIds = relation.wayMembers.map(_.ref)
          relationWayIds.flatMap { wayId =>
            rawData.ways.find(_.id == wayId) match {
              case Some(way) => Some(way)
              case None =>
                //noinspection SideEffectsInMonadicTransformation
                println(s"Inconsistant data: could not find way $wayId")
                None
            }
          }
        }

        val nodes = {
          val wayNodeIds = ways.flatMap(_.nodeIds)
          val relationNodeIds = relation.nodeMembers.map(_.ref)
          val nodeIds = (wayNodeIds ++ relationNodeIds).distinct
          nodeIds.flatMap { nodeId =>
            rawData.nodes.find(_.id == nodeId) match {
              case Some(node) => Some(node)
              case None =>
                //noinspection SideEffectsInMonadicTransformation
                println(s"Inconsistant data: could not find node $nodeId")
                None
            }
          }
        }

        val extract = RawData(None, nodes, ways, Seq(relation))

        val out = new PrintWriter(new File(outputFileName))
        new OsmDataXmlWriter(extract, out).print()
        out.close()
    }
  }
}
