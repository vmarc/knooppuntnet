package kpn.core.engine.analysis

import kpn.shared.data.Tag
import kpn.shared.data.Tags
import kpn.shared.data.Way
import kpn.shared.route.Backward
import kpn.shared.route.Both
import kpn.shared.route.Forward
import kpn.shared.route.WayDirection

object WayAnalyzer {

  def isRoundabout(way: Way): Boolean = way.tags.has("junction", "roundabout")

  def isClosedLoop(way: Way): Boolean = {
    way.nodes.size > 2 && way.nodes.head == way.nodes.last
  }

  def isSelfIntersecting(way: Way): Boolean = {
    way.nodes.size > 2 &&
      (way.nodes.toSet.size != way.nodes.size) &&
      way.nodes.head != way.nodes.last
  }

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

  def oneWay(way: Way): WayDirection = {

    if (isOneWayBicycleNo(way)) {
      Both
    }
    else if (isOppositeTrack(way)) {
      Both
    }
    else if (way.tags.has("cycleway:left", "lane", "track") && way.tags.has("cycleway:right", "lane", "track")) {
      Both
    }
    else if (isOneWayYes(way) && isOppositeLane(way)) {
      Both
    }
    else if (isOneWayReverse(way) && !(isOneWayBicycleNo(way) || isOneWayBicycleNo2(way))) {
      Backward
    }
    else if (isOneWayBicycleYes(way)) {
      Forward
    }
    else if (isOneWayYes(way) && !(isOneWayBicycleNo(way) || isOneWayBicycleNo2(way))) {
      Forward
    }
    else if (isOneWayYes(way) && way.tags.has("cycleway:left", "lane", "track")) {
      Both
    }
    else if (isOneWayReverse(way) && way.tags.has("cycleway:left", "lane", "track")) {
      Backward
    }
    else if (isOneWayYes(way) && way.tags.has("cycleway:right", "lane", "track")) {
      Forward
    }
    else if (isOneWayReverse(way) && way.tags.has("cycleway:right", "lane", "track")) {
      Forward
    }
    else if (isOneWayBicycleReverse(way)) {
      Backward
    }
    else {
      Both
    }
  }

  private def isOneWayBicycleNo(way: Way): Boolean = {
    way.tags.has("oneway:bicycle", "no", "0", "false") ||
      way.tags.has("bicycle:oneway", "no", "0", "false")
  }

  private def isOneWayBicycleNo2(way: Way): Boolean = {
    way.tags.has("cycleway", "opposite") ||
      way.tags.has("cycleway:left") ||
      way.tags.has("cycleway:right")
  }

  private def isOneWayBicycleYes(way: Way): Boolean = {
    way.tags.has("oneway:bicycle", "yes", "1", "true") ||
      way.tags.has("bicycle:oneway", "yes", "1", "true")
  }

  private def isOneWayBicycleReverse(way: Way): Boolean = {
    way.tags.has("oneway:bicycle", "-1", "reverse") ||
      way.tags.has("bicycle:oneway", "-1", "reverse")
  }

  private def isOneWayYes(way: Way): Boolean = {
    way.tags.has("oneway", "yes", "1", "true")
  }

  private def isOneWayReverse(way: Way): Boolean = {
    way.tags.has("oneway", "-1", "reverse")
  }

  private def isOppositeLane(way: Way): Boolean = {
    way.tags.has("cycleway", "opposite") ||
      way.tags.has("cycleway", "opposite_lane") ||
      way.tags.has("cycleway:left", "opposite_lane") ||
      way.tags.has("cycleway:right", "opposite_lane")
  }

  private def isOppositeTrack(way: Way): Boolean = {
    way.tags.has("cycleway", "opposite_track") ||
      way.tags.has("cycleway:left", "opposite_track") ||
      way.tags.has("cycleway:right", "opposite_track")
  }
}
