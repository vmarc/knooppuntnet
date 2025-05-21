package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.route.LinkDirection
import kpn.server.analyzer.engine.analysis.route.structure.reference.JavaLink

object JavaDirectionConverter {
  def toScala(direction: JavaLink.Direction): LinkDirection = direction match {
    case JavaLink.Direction.FORWARD => LinkDirection.Forward
    case JavaLink.Direction.BACKWARD => LinkDirection.Backward
    case JavaLink.Direction.ROUNDABOUT_LEFT => LinkDirection.RoundaboutLeft
    case JavaLink.Direction.ROUNDABOUT_RIGHT => LinkDirection.RoundaboutRight
    case JavaLink.Direction.NONE => LinkDirection.Unconnected
    case unknown => throw new IllegalArgumentException(s"Unknown reference direction: $unknown")
  }
}
