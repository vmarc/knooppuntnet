package kpn.core.tools.next.support.compare

import kpn.core.doc.OldRouteDoc
import kpn.core.doc.RouteDetailDoc
import kpn.core.util.Log

class CompareLabels(oldRouteDoc: OldRouteDoc, newRouteDoc: RouteDetailDoc, log: Log) {
  def compare(): Unit = {
    if (oldRouteDoc.labels.toSet != newRouteDoc.labels.toSet) {
      val oldLabels = s"""old-labels=${oldRouteDoc.labels.mkString(", ")}"""
      val newLabels = s"""new-labels=${newRouteDoc.labels.mkString(", ")}"""
      log.info(s"labels mismatch\n$oldLabels\n$newLabels")
    }
  }
}
