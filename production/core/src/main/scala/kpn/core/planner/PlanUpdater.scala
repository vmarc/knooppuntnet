package kpn.core.planner

import kpn.core.planner.graph.NodeNetworkGraph

/*

  Example use:

    following is needed for bicycle and hiking separately (separate graphs)

    val graph: Graph[...] = ...
    val repository: Repository = ...
    val planUpdater = new PlanUpdater(graph)
    val planBuilder = new PlanBuilder(repository)

    val nodeId = "4"

    val encodedPlanString = "N1-R1-N2-R2-N3"
    val encodedPlan: EncodedPlan = EncodedPlan(encodedPlanString) ==> catch Exception OR Left/Right?
    val updatedEncodedPlan: EncodedPlan = planUpdater.add(encodedPlan, nodeId)
    val plan: Plan = planBuilder.build(updatedEncodedPlan)
*/

/*
  at server start, build graph of all nodes and routes
    refresh mechanism:
      - once a week after analysis was done?
      - once per hour once more frequent updates are performed
*/

/*
 * request contains:
 *   - a honey pot (this should be handled in Planner.scala?
 *   - identification of the node that was clicked
 *   - the structure of the route so far
 *       - via nodes + nodes passed through
 *       -
 *       -
 *       -
 *
 * if (no or empty structure in request) {
 *   create fresh structure with the clicked node as starting point
 * }
 * else {
 *   calculate the shortest path from the last node in the structure to the newly selected node
 *
 * }
 *
 */

/*

    What goes back should contain:      SHOULD THIS BE DONE IN 2 SEPARATE REQUESTS?
      - formatted html that shows in a list
          - all nodes passed through
          - via nodes formatted in a different way
          - ferry symbol where route contains ferry
          - distance between nodes
          - accumulated distance
          - total distance at the bottom

      - all that is needed to draw the route on the map ==> this should update the leaflet javascript objects
          - all line segments that together form the route  (put in separate layer, and destroy/re-create the layer all the time)


 example encoding of complete track: +1111-2222-3333+4444-5555-6666+7777+8888

     --> numbers with + in front are node-ids of nodes clicked by the user
     --> numbers with - in front are node-ids of intermediate nodes selected by the shortest path algorithm

 example II - also include routeIds: +1111(aaaa)-2222(bbbb)-3333(cccc)+4444(dddd)-5555(eeee)-6666(ffff)+7777(ggggg)+8888

 example III - same info in json:
*/

case class RouteEdge(routeId: Long, startNodeId: Long, endNodeId: Long)


class PlanUpdater(graph: NodeNetworkGraph) {

  /**
    * Appends the path to given node to given plan.
    */
  def add(before: EncodedPlan, nodeId: Long): EncodedPlan = {

    if (before.userNodeIds.isEmpty) {
      EncodedPlan(Seq(EncodedNode(nodeId)))
    }
    else {
      val fromNodeId = before.userNodeIds.last
      graph.findPath(fromNodeId, nodeId) match {
        case None => before.add(PathNotFoundInGraph(fromNodeId, nodeId))
        case Some(path) =>
          EncodedPlan(Seq())

        //          var nId = lastNodeId
        //          val routeEdges = path.edges.toSeq.map { edge =>
        //
        //            val routeId: Long = edge.label match {
        //              case x: Long => x
        //              case x: String => throw new RuntimeException("unexpected value")
        //            }
        //
        //            RouteEdge(routeId, edge._1, edge._2)
        //          }
        //
        //          val sorted = routeEdges.map { edge =>
        //            if (edge.startNodeId == nId) {
        //              nId = edge.endNodeId
        //              edge
        //            }
        //            else {
        //              nId = edge.startNodeId
        //              RouteEdge(edge.routeId, edge.endNodeId, edge.startNodeId)
        //            }
        //          }
        //
        //          val intermediateItems = sorted.init.flatMap { edge =>
        //            Seq(EncodedRoute(edge.routeId), EncodedIntermediateNode(edge.endNodeId))
        //          }
        //          val lastEdge = sorted.last
        //          val lastItems = Seq(EncodedRoute(lastEdge.routeId), EncodedNode(lastEdge.endNodeId))
        //          before.add(intermediateItems ++ lastItems)
      }
    }
  }

  /**
    * Removes the last leg from given plan: removes all items between the end of the plan
    * and the last user selected node (this means that any routes and intermediate nodes
    * are removed).
    */
  def undo(encodedPlan: EncodedPlan): EncodedPlan = {
    val reversed = encodedPlan.items.reverse
    val items = if (reversed.isEmpty) {
      Seq()
    }
    else {
      val itemsWithoutEndNode = reversed.tail
      if (itemsWithoutEndNode.isEmpty) {
        Seq()
      }
      else {
        itemsWithoutEndNode.dropWhile {
          case n: EncodedNode => false
          case _ => true
        }.reverse
      }
    }
    EncodedPlan(items)
  }

  def reset(encodedPlan: EncodedPlan): EncodedPlan = {
    EncodedPlan(Seq())
  }
}
