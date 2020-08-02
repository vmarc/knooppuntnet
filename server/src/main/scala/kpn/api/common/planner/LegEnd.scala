package kpn.api.common.planner

import kpn.api.common.common.ToStringBuilder
import kpn.api.common.common.TrackPathKey

object LegEnd {

  def node(nodeId: Long): LegEnd = {
    LegEnd(Some(LegEndNode(nodeId)), None)
  }

  def route(trackPathKeys: Seq[TrackPathKey], selection: Option[TrackPathKey] = None): LegEnd = {
    route(LegEndRoute(trackPathKeys, selection))
  }

  def route(legEndRoute: LegEndRoute): LegEnd = {
    LegEnd(None, Some(legEndRoute))
  }

  def fromPlanString(planString: String, encoded: Boolean = true): Seq[LegEnd] = {

    if (planString.isEmpty) {
      Seq()
    }
    else {

      def fragmentToLong(value: String): Long = {
        try {
          val radix = if (encoded) 36 else 10
          java.lang.Long.parseLong(value, radix)
        }
        catch {
          case e: NumberFormatException =>
            throw new IllegalArgumentException(s"Could not interprete planString '$planString'", e)
        }
      }

      planString.split("-").toSeq.flatMap { fragment =>
        if (fragment.contains("|")) {
          fragment.split("\\|") match {
            case subFragments =>
              val paths = subFragments.toSeq.flatMap { subFragment =>
                subFragment.split("\\.") match {
                  case Array(routeIdFragment, pathIdFragment) =>
                    val routeId = fragmentToLong(routeIdFragment)
                    val pathId = fragmentToLong(pathIdFragment)
                    Some(TrackPathKey(routeId, pathId))
                  case _ => None
                }
              }
              Some(LegEnd.route(LegEndRoute(paths, None)))

            case _ => None
          }
        }
        else if (fragment.contains(".")) {
          fragment.split("\\.") match {
            case Array(routeIdFragment, pathIdFragment) =>
              val routeId = fragmentToLong(routeIdFragment)
              val pathId = fragmentToLong(pathIdFragment)
              Some(LegEnd.route(List(TrackPathKey(routeId, pathId))))
            case _ => None
          }
        }
        else {
          val nodeId = fragmentToLong(fragment)
          Some(LegEnd.node(nodeId))
        }
      }
    }
  }

  def fromString(string: String): LegEnd = {
    if (string.contains(".")) {
      string.split("\\.") match {
        case Array(routeIdString, pathIdString) =>
          val routeId = routeIdString.toLong
          val pathId = pathIdString.toLong
          LegEnd.route(List(TrackPathKey(routeId, pathId)))
        case _ => null
      }
    }
    else {
      node(string.toLong)
    }
  }

}

case class LegEnd(
  node: Option[LegEndNode],
  route: Option[LegEndRoute]
) {

  def vertices: Seq[String] = {
    node match {
      case Some(legEndNode) => Seq(legEndNode.nodeId.toString)
      case None =>
        route match {
          case None => throw new IllegalStateException("LegEnd missing both node and route")
          case Some(legEndRoute) => legEndRoute.trackPathKeys.map(_.key)
        }
    }
  }

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("node", node).
    field("route", route).
    build

}
