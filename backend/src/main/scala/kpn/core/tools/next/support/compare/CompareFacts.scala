package kpn.core.tools.next.support.compare

import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.OldRouteDoc
import kpn.core.util.Log

class CompareFacts(oldRouteDoc: OldRouteDoc, newBaseRouteDoc: BaseRouteDoc, log: Log) {

  def compare(): Unit = {
    if (newBaseRouteDoc.segments.sizeIs != 1) {
      // cannot compare facts
      return
    }
    if (oldRouteDoc.facts.toSet != newBaseRouteDoc.facts.toSet) {
      val oldFacts = s"""old-facts=${oldRouteDoc.facts.mkString(", ")}"""
      val newFacts = s"""new-facts=${newBaseRouteDoc.facts.mkString(", ")}"""
      log.info(s"facts mismatch\n$oldFacts\n$newFacts")
    }
  }
}
