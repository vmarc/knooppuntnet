package kpn.server.analyzer.engine.analysis

import kpn.shared.data.Tag
import kpn.shared.data.Tags
import kpn.shared.data.Way
import kpn.shared.route.Backward
import kpn.shared.route.Both
import kpn.shared.route.Forward
import kpn.shared.route.WayDirection

object OneWayAnalyzer {
  def oneWayTags(way: Way): Tags = {
    val tags = way.tags.tags.filter { case Tag(key, value) =>
      Seq("oneway", "oneway:bicycle", "bicycle:oneway").contains(key) ||
        (key == "junction" && value == "roundabout") ||
        (key == "cycleway" && value == "opposite") ||
        (key == "cyclewhay" && value == "opposite_lane") ||
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
    Tags(tags)
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
    way.tags.has("oneway:bicycle", "no", "0", "false") ||
      way.tags.has("bicycle:oneway", "no", "0", "false")
  }

  private def isOneWayBicycleNoCycleWay: Boolean = {
    way.tags.has("cycleway", "opposite") ||
      way.tags.has("cycleway:left") ||
      way.tags.has("cycleway:right")
  }

  private def isOneWayBicycleYes: Boolean = {
    way.tags.has("oneway:bicycle", "yes", "1", "true") ||
      way.tags.has("bicycle:oneway", "yes", "1", "true")
  }

  private def isOneWayBicycleReverse: Boolean = {
    way.tags.has("oneway:bicycle", "-1", "reverse") ||
      way.tags.has("bicycle:oneway", "-1", "reverse")
  }

  private def isOneWayYes: Boolean = {
    way.tags.has("oneway", "yes", "1", "true")
  }

  private def isOneWayReverse: Boolean = {
    way.tags.has("oneway", "-1", "reverse")
  }

  private def isOppositeLane: Boolean = {
    way.tags.has("cycleway", "opposite") ||
      way.tags.has("cycleway", "opposite_lane") ||
      way.tags.has("cycleway:left", "opposite_lane") ||
      way.tags.has("cycleway:right", "opposite_lane")
  }

  private def isOppositeTrack: Boolean = {
    way.tags.has("cycleway", "opposite_track") ||
      way.tags.has("cycleway:left", "opposite_track") ||
      way.tags.has("cycleway:right", "opposite_track")
  }

  private def isCycleLaneLeftAndRight: Boolean = {
    way.tags.has("cycleway:left", "lane", "track") &&
      way.tags.has("cycleway:right", "lane", "track")
  }

  private def cycleWayLeft: Boolean = {
    way.tags.has("cycleway:left", "lane", "track")
  }

  private def cycleWayRight: Boolean = {
    way.tags.has("cycleway:right", "lane", "track")
  }

  private def isRoundabout: Boolean = {
    way.tags.has("junction", "roundabout")
  }

}
