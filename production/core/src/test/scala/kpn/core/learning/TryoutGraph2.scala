package kpn.core.learning

import kpn.core.db.couch.Couch

import scalax.collection.edge.WLUnDiEdge
import scalax.collection.immutable.Graph

object TryoutGraph2 {
  def main(args: Array[String]): Unit = {
    new TryoutGraph2().run()
  }
}

class TryoutGraph2 {
  def run(): Unit = {
    println("opening database")
    Couch.executeIn("testdb") { database =>

      //      val repository = new RepositoryImpl(database)
      //      val graphRepository = new GraphRepositoryImpl(repository)
      //
      //      println("initializing")
      //      val t1 = System.currentTimeMillis()
      //      val edges = graphRepository.edges(NetworkType.bicycle)
      //      val t2 = System.currentTimeMillis()
      //      val nodes = edges.flatten //Map(edge => Seq(edge._1)
      //      val t3 = System.currentTimeMillis()
      //      val graph = Graph.from(nodes, edges)
      //      val t4 = System.currentTimeMillis()
      //
      //      printf("%d edges loaded in %dms, %d nodes collected in %dms, graph created in %dms\n",
      //          edges.size, (t2 - t1), nodes.size, (t3 -t2), (t4 - t3))
      //
      //      println("test:")
      //
      //      shortestPath(repository, graph, "307163434", "859346121") // rcn
      //      //shortestPath(repository, graph, "42966926" /*41*/, "1725257722" /*38*/) //rwn

    }
  }

  private def shortestPath(graph: Graph[String, WLUnDiEdge], from: String, to: String): Unit = {
    val t1 = System.currentTimeMillis()
    graph.find(from) match {
      case None => println("from node not defined: " + from)
      case Some(fromNode) =>
        graph.find(to) match {
          case None => println("to node not defined: " + to)
          case Some(toNode) =>
            fromNode.shortestPathTo(toNode) match {
              case None => printf("no path between %s and %s\n", from, to)
              case Some(path) =>

                val t2 = System.currentTimeMillis()
                printf("path calculated in %dms\n", t2 - t1)

                printf("from=%s, to=%s, path=%s\n", from, to, path.nodes.mkString(" - "))
                path.edges.foreach { edge =>
                  val routeName = "bla"

                  // TODO measure difference in performance between picking up object from db one by one, or all in one go

                  //                  val routeName = repository.getDocById[RouteDoc]("R" + edge.label) match {
                  //                    case Some(route) => route.route.name
                  //                    case None => "?"
                  //                  }
                  printf("  route=%s, from=%s, to=%s, weight=%d\n", routeName, edge._1, edge._2, edge.weight)
                }
            }
        }
    }
  }
}
