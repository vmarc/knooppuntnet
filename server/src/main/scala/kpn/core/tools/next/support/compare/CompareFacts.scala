package kpn.core.tools.next.support.compare

import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.OldRouteDoc
import kpn.core.util.Log

class CompareFacts(oldRouteDoc: OldRouteDoc, newRouteDoc: BaseRouteDoc, log: Log) {

  def compare(): Unit = {
    if (newRouteDoc.segments.sizeIs != 1) {
      // cannot compare facts
      return
    }
    if (oldRouteDoc.facts.toSet != newRouteDoc.facts.toSet) {
      val oldFacts = s"""old-facts=${oldRouteDoc.facts.mkString(", ")}"""
      val newFacts = s"""new-facts=${newRouteDoc.facts.mkString(", ")}"""
      log.info(s"facts mismatch\n$oldFacts\n$newFacts")
    }
  }
}
