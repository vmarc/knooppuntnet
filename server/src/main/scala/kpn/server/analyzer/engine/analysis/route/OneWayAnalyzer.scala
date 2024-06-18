package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.data.Way
import kpn.api.common.route.Backward
import kpn.api.common.route.Both
import kpn.api.common.route.Forward
import kpn.api.common.route.WayDirection
import kpn.api.custom.Tag

object OneWayAnalyzer {
  def oneWayTags(way: Way): Seq[Tag] = {
    way.tags.filter { case Tag(key, value) =>
      Seq("oneway", "oneway:bicycle", "bicycle:oneway").contains(key) ||
        (key == "junction" && value == "roundabout") ||
        (key == "cycleway" && value == "opposite") ||
        (key == "cycleway" && value == "opposite_lane") ||
        (key == "cycleway" && value == "opposite_track") ||
        (key == "cycleway:left" && value == "lane") ||
        (key == "cycleway:left" && value == "track") ||
        (key == "cycleway:left" && value == "opposite_lane") ||
        (key == "cycleway:left" && value == "opposite_track") ||
        (key == "cycleway:right" && value == "lane") ||
        (key == "cycleway:right" && value == "track") ||
        (key == "cycleway:right" && value == "opposite_lane") ||
        (key == "cycleway:right" && value == "opposite_track")
    }
  }
}

class OneWayAnalyzer(way: Way) {

  def direction: WayDirection = {

    if (isOneWayBicycleNo) {
      Both
    }
    else if (isOppositeTrack) {
      Both
    }
    else if (isCycleLaneLeftAndRight) {
      Both
    }
    else if (isOneWayYes && isOppositeLane) {
      Both
    }
    else if (isOneWayReverse && !(isOneWayBicycleNo || isOneWayBicycleNoCycleWay)) {
      Backward
    }
    else if (isRoundabout) {
      Forward
    }
    else if (isOneWayBicycleYes) {
      Forward
    }
    else if (isOneWayYes && !(isOneWayBicycleNo || isOneWayBicycleNoCycleWay)) {
      Forward
    }
    else if (isOneWayYes && cycleWayLeft) {
      Both
    }
    else if (isOneWayReverse && cycleWayLeft) {
      Backward
    }
    else if (isOneWayYes && cycleWayRight) {
      Forward
    }
    else if (isOneWayReverse && cycleWayRight) {
      Forward
    }
    else if (isOneWayBicycleReverse) {
      Backward
    }
    else {
      Both
    }
  }

  private def isOneWayBicycleNo: Boolean = {
    way.hasTag("oneway:bicycle", "no", "0", "false") ||
      way.hasTag("bicycle:oneway", "no", "0", "false")
  }

  private def isOneWayBicycleNoCycleWay: Boolean = {
    way.hasTag("cycleway", "opposite") ||
      way.hasTag("cycleway:left") ||
      way.hasTag("cycleway:right")
  }

  private def isOneWayBicycleYes: Boolean = {
    way.hasTag("oneway:bicycle", "yes", "1", "true") ||
      way.hasTag("bicycle:oneway", "yes", "1", "true")
  }

  private def isOneWayBicycleReverse: Boolean = {
    way.hasTag("oneway:bicycle", "-1", "reverse") ||
      way.hasTag("bicycle:oneway", "-1", "reverse")
  }

  private def isOneWayYes: Boolean = {
    way.hasTag("oneway", "yes", "1", "true")
  }

  private def isOneWayReverse: Boolean = {
    way.hasTag("oneway", "-1", "reverse")
  }

  private def isOppositeLane: Boolean = {
    way.hasTag("cycleway", "opposite") ||
      way.hasTag("cycleway", "opposite_lane") ||
      way.hasTag("cycleway:left", "opposite_lane") ||
      way.hasTag("cycleway:right", "opposite_lane")
  }

  private def isOppositeTrack: Boolean = {
    way.hasTag("cycleway", "opposite_track") ||
      way.hasTag("cycleway:left", "opposite_track") ||
      way.hasTag("cycleway:right", "opposite_track")
  }

  private def isCycleLaneLeftAndRight: Boolean = {
    way.hasTag("cycleway:left", "lane", "track") &&
      way.hasTag("cycleway:right", "lane", "track")
  }

  private def cycleWayLeft: Boolean = {
    way.hasTag("cycleway:left", "lane", "track")
  }

  private def cycleWayRight: Boolean = {
    way.hasTag("cycleway:right", "lane", "track")
  }

  private def isRoundabout: Boolean = {
    way.hasTag("junction", "roundabout")
  }
}
