package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.data.Way
import kpn.core.util.Triplet

case class WayTriplet(delegate: Triplet[Way]) {

  override def toString: String = {
    def option(o: Option[Way]) = o match {
      case None => "None"
      case Some(way) => way.id
    }
    val a = option(delegate.previous)
    val b = delegate.current.id
    val c = option(delegate.next)
    s"Triplet($a, $b, $c)"
  }

}
