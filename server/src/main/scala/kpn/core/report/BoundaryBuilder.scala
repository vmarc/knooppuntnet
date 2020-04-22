package kpn.core.report

import java.io.File
import java.io.PrintWriter

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.sys.process.Process

class BoundaryBuilder {

  case class Edge(start: Int, end: Int)

  def boundary(nodes: Seq[LatLon]): Seq[LatLon] = {
    writeNodeFile(nodes)
    triangulate()
    val edges: Seq[Edge] = readEdges() // sorted
    removeTempFiles()
    edges.map(e => nodes(e.start))
  }

  private def writeNodeFile(nodes: Seq[LatLon]): Unit = {
    val fileName = "/tmp/boundary.node"
    printf("Save boundaries: %s\n", fileName)
    val file = new File(fileName)
    val pw = new PrintWriter(file)
    pw.println(s"${nodes.size} 2 0 0")
    nodes.zipWithIndex.foreach { case (node, index) =>
      pw.println("%d %f %f".format( index, node.longitude, node.latitude))
    }
    pw.close()
  }

  private def triangulate(): Unit = {
    val cmd = "/kpn/Triangle/triangle -e -E -N /tmp/boundary.node"
    val contents = Process(cmd).lazyLines.mkString("=== ", "\n=== ", "")
    println("---")
    //    println(contents)
    //    println("---")
  }

  private def readEdges(): Seq[Edge] = {
    val fileName = "/tmp/boundary.1.edge"

    println("Reading " + fileName)

    val source = Source.fromFile(fileName)
    val lines = source.getLines().toList.filterNot(line => line.startsWith("#") || line.endsWith("0") || line.length < 8)
    source.close()

    val edges = lines.map { line =>
      val splitted = line.split(" +").toSeq
      Edge(splitted(2).toInt, splitted(3).toInt)
    }

    println(edges)

    if (edges.isEmpty) {
      Seq()
    }
    else {
      val sorted = ListBuffer[Edge]()
      var currentEdge = edges.head
      sorted += currentEdge
      edges.tail.foreach { ee =>
        edges.find(e => e.start == currentEdge.end) match {
          case Some(e) =>
            sorted += e
            currentEdge = e
          case _ =>
        }
      }
      sorted.toSeq
    }
  }

  private def removeTempFiles(): Unit = {
    new File("/tmp/boundary.node").delete()
    new File("/tmp/boundary.1.edge").delete()
    ()
  }
}
