package kpn.core.engine.analysis

import kpn.shared.SharedTestObjects
import kpn.shared.data.Tags
import kpn.shared.route.Backward
import kpn.shared.route.Both
import kpn.shared.route.Forward
import kpn.shared.route.WayDirection
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OneWayAnalyzerTest extends FunSuite with Matchers with SharedTestObjects {

  test("way without oneway related tags can be traveled in both directions in all networktypes") {
    oneway() should equal(Both)
  }

  // testcases based on http://wiki.openstreetmap.org/wiki/Key:oneway

  test("oneway in forward direction of way for all vehicles") {
    oneway("oneway" -> "yes") should equal(Forward)
    oneway("oneway" -> "1") should equal(Forward)
    oneway("oneway" -> "true") should equal(Forward)
  }

  test("oneway in backward direction of way for all vehicles") {
    oneway("oneway" -> "-1") should equal(Backward)
    oneway("oneway" -> "reverse") should equal(Backward) // discouraged alternative
  }

  test("tag 'oneway=no' confirms that the way can be travelled in both directions") {
    oneway("oneway" -> "no") should equal(Both)
    oneway("oneway" -> "false") should equal(Both) // discouraged alternative
    oneway("oneway" -> "0") should equal(Both) // discouraged alternative
  }

  test("oneway:bicycle overrides oneway") {
    oneway("oneway" -> "yes", "oneway:bicycle" -> "no") should equal(Both)
    oneway("oneway" -> "yes", "oneway:bicycle" -> "false") should equal(Both)
    oneway("oneway" -> "yes", "oneway:bicycle" -> "0") should equal(Both)
    oneway("oneway" -> "-1", "oneway:bicycle" -> "no") should equal(Both)
    oneway("oneway" -> "-1", "oneway:bicycle" -> "false") should equal(Both)
    oneway("oneway" -> "-1", "oneway:bicycle" -> "0") should equal(Both)

    // following combinations are probably not useful in practice
    oneway("oneway" -> "no", "oneway:bicycle" -> "yes") should equal(Forward)
    oneway("oneway" -> "no", "oneway:bicycle" -> "true") should equal(Forward)
    oneway("oneway" -> "no", "oneway:bicycle" -> "1") should equal(Forward)
    oneway("oneway" -> "no", "oneway:bicycle" -> "-1") should equal(Backward)
    oneway("oneway" -> "no", "oneway:bicycle" -> "reverse") should equal(Backward)
  }

  test("bicycle:oneway is a synonym for oneway:bicycle") {
    oneway("oneway" -> "yes", "bicycle:oneway" -> "no") should equal(Both)
    oneway("oneway" -> "yes", "bicycle:oneway" -> "false") should equal(Both)
    oneway("oneway" -> "yes", "bicycle:oneway" -> "0") should equal(Both)
    oneway("oneway" -> "reverse", "bicycle:oneway" -> "no") should equal(Both)
    oneway("oneway" -> "reverse", "bicycle:oneway" -> "false") should equal(Both)
    oneway("oneway" -> "reverse", "bicycle:oneway" -> "0") should equal(Both)
    oneway("oneway" -> "no", "bicycle:oneway" -> "yes") should equal(Forward)
    oneway("oneway" -> "no", "bicycle:oneway" -> "true") should equal(Forward)
    oneway("oneway" -> "no", "bicycle:oneway" -> "1") should equal(Forward)
    oneway("oneway" -> "no", "bicycle:oneway" -> "-1") should equal(Backward)
    oneway("oneway" -> "no", "bicycle:oneway" -> "reverse") should equal(Backward)
  }

  test("oneway:bicycle used on its own") {
    // probably should not be done in practice
    oneway("oneway:bicycle" -> "yes") should equal(Forward)
    oneway("oneway:bicycle" -> "true") should equal(Forward)
    oneway("oneway:bicycle" -> "1") should equal(Forward)
    oneway("oneway:bicycle" -> "-1") should equal(Backward)
    oneway("oneway:bicycle" -> "reverse") should equal(Backward)
    oneway("oneway:bicycle" -> "no") should equal(Both)
    oneway("oneway:bicycle" -> "false") should equal(Both)
    oneway("oneway:bicycle" -> "0") should equal(Both)
  }

  test("oneway:bicycle same as oneway has no effect") {
    oneway("oneway" -> "yes", "oneway:bicycle" -> "yes") should equal(Forward)
    oneway("oneway" -> "-1", "oneway:bicycle" -> "-1") should equal(Backward)
    oneway("oneway" -> "no", "oneway:bicycle" -> "no") should equal(Both)
  }

  /*
    A cycleway=* tag can be added to a highway=* to map cycling infrastructure that is an inherent part of the road.
    This specifically applies to cycle lanes which are always a part of the road, and often also applies to separated
    cycle tracks if they are running parallel and next to the road.
  */

  /*
    Wiki: cycleway=lane is used to tag two-way streets where there are cycle lanes on both sides of the road,
    or one-way streets where there is a lane operating in the direction of main traffic flow.
  */
  test("tag 'cycleway=lane' does not override the 'oneway'") {
    oneway("cycleway" -> "lane", "oneway" -> "yes") should equal(Forward)
    oneway("cycleway" -> "lane", "oneway" -> "-1") should equal(Backward)
    oneway("cycleway" -> "lane", "oneway" -> "no") should equal(Both)
    oneway("cycleway" -> "lane") should equal(Both)
  }

  /*
    Wiki: Consider using the cycleway:left=lane and/or cycleway:right=lane tags for a cycle lane which
    is on the left and/or right side, relative to the direction in which the way was drawn in the editor,
    as this describes on which side the cycle lane is. It should then be assumed that cycle traffic is allowed
    to flow in the customary direction for traffic on that side of the road.
  */
  test("cycleway:left=lane - there is a lane in the opposite direction of the way (assume driving on the right)") {
    oneway("oneway" -> "yes", "cycleway:left" -> "lane") should equal(Both)
    oneway("oneway" -> "-1", "cycleway:left" -> "lane") should equal(Backward)
  }

  test("cycleway:right=lane - there is a lane in the direction of the way (assume driving on the right)") {
    oneway("oneway" -> "yes", "cycleway:right" -> "lane") should equal(Forward)
    oneway("oneway" -> "-1", "cycleway:right" -> "lane") should equal(Forward)
  }

  test("with both cycleway:left=lane and cycleway:right=lane can travel in both directions") {
    oneway("cycleway:left" -> "lane", "cycleway:right" -> "lane") should equal(Both)
    oneway("oneway" -> "yes", "cycleway:left" -> "lane", "cycleway:right" -> "lane") should equal(Both)
    oneway("oneway" -> "-1", "cycleway:left" -> "lane", "cycleway:right" -> "lane") should equal(Both)
  }

  /*
    Wiki: Use cycleway=opposite_lane for a contraflow cycle lane, that is, a cycle lane travelling in the opposite
    direction to other traffic on a oneway=yes road. Consider using the cycleway:left=opposite_lane or
    cycleway:right=opposite_lane tags instead, as this describes on which side the contraflow lane is.
  */
  test("a contraflow cycle lane overrides oneway=yes") {
    oneway("oneway" -> "yes", "cycleway" -> "opposite_lane") should equal(Both)
    oneway("oneway" -> "yes", "cycleway:left" -> "opposite_lane") should equal(Both)
    oneway("oneway" -> "yes", "cycleway:right" -> "opposite_lane") should equal(Both)
  }

  /*
    Wiki: Use cycleway=opposite for situations where cyclists are permitted to travel in both directions on a
    road which is one-way for normal traffic, in situations where there is no dedicated contra-flow lane
    marked for cyclists.
  */
  test("cycleway:opposite can travel in both directions") {
    oneway("oneway" -> "yes", "cycleway" -> "opposite") should equal(Both)
    oneway("oneway" -> "-1", "cycleway:left" -> "lane", "cycleway:right" -> "lane") should equal(Both)
  }

  /*
    Wiki: cycleway=track indicates a cycle track which is associated with a highway. As with cycle lanes, you can
    use cycleway:left=track and cycleway:right=track to indicate on which side of the road the track lies,
    relative to the direction in which the way was drawn in the editor.
  */
  test("a cycleway=track does not alter the oneway property") {
    oneway("oneway" -> "yes", "cycleway" -> "track") should equal(Forward)
    oneway("oneway" -> "no", "cycleway" -> "track") should equal(Both)
    oneway("oneway" -> "-1", "cycleway" -> "track") should equal(Backward)
  }

  /*
    Wiki: Use cycleway=opposite_track for a contraflow cycle track, that is, a cycle track travelling in the
      opposite direction to other traffic on a oneway=yes road. Consider using the cycleway:left=opposite_track
      or cycleway:right=opposite_track tags instead, as this describes on which side the contraflow track is.
  */
  test("a contraflow cycle track overrides oneway=yes") {
    oneway("oneway" -> "yes", "cycleway" -> "opposite_track") should equal(Both)
    oneway("oneway" -> "yes", "cycleway:left" -> "opposite_track") should equal(Both)
    oneway("oneway" -> "yes", "cycleway:right" -> "opposite_track") should equal(Both)
  }

  test("combination from practice") {
    oneway("cycleway" -> "opposite", "oneway" -> "yes", "cycleway:left" -> "lane") should equal(Both)
  }

  /* Jarien email 2015-09-06 - support added for cycleway:left=track and cycleway:right=track */

  test("St. Elisabethstraat (7074930) v.9") {
    oneway("oneway" -> "yes", "oneway:bicycle" -> "no") should equal(Both)
  }

  test("St. Elisabethstraat (7074930) v.8") {
    oneway("cycleway" -> "opposite", "oneway" -> "yes", "oneway:bicycle" -> "no") should equal(Both)
  }

  test("St. Elisabethstraat (7074930) v.7") {
    oneway("cycleway" -> "opposite", "oneway" -> "yes") should equal(Both)
  }

  test("Esschestraat (7074935) v9") {
    oneway("oneway" -> "yes", "oneway:bicycle" -> "no") should equal(Both)
  }

  test("Esschestraat (7074935) v8") {
    oneway(
      "cycleway:left" -> "track",
      "cycleway:right" -> "lane",
      "cycleway:surface" -> "asphalt",
      "cycleway:surface:color" -> "red",
      "oneway" -> "yes",
      "oneway:bicycle" -> "no"
    ) should equal(Both)
  }

  test("Esschestraat (7074935) v7") {
    oneway(
      "cycleway:left" -> "track",
      "cycleway:right" -> "lane",
      "cycleway:surface" -> "asphalt",
      "cycleway:surface:color" -> "red",
      "oneway" -> "yes"
    ) should equal(Both)
  }

  /* A67-A67 email 2017-07-25 */

  test("Poortendries (way 50053949)") {
    oneway(
      "cycleway:right" -> "track",
      "oneway" -> "yes",
      "oneway:bicycle" -> "no"
    ) should equal(Both)
  }

  /* A67-A67 Issue #31 oneway:bicycle=no on roundabout */

  test("Issue #31 junction=roundabout included in OneWayAnalyzer") {
    oneway(
      "junction" -> "roundabout"
    ) should equal(Forward)
  }

  test("Issue #31 junction=roundabout overruled by oneway:bicycle=no") {
    oneway(
      "junction" -> "roundabout",
      "oneway:bicycle" -> "no"
    ) should equal(Both)
  }

  /* Examples on: http://wiki.openstreetmap.org/wiki/Bicycle */

  test("L1a") {
    oneway(
      "cycleway" -> "lane"
    ) should equal(Both)

    oneway(
      "cycleway:left" -> "lane",
      "cycleway:right" -> "lane"
    ) should equal(Both)

    oneway(
      "cycleway:both" -> "lane"
    ) should equal(Both)
  }

  test("L1b") {
    oneway(
      "cycleway:right" -> "lane",
      "oneway:bicycle" -> "no"
    ) should equal(Both)

    oneway(
      "cycleway" -> "lane"
    ) should equal(Both)
  }

  test("L2") {
    /*
      Oneway cycle lane on right side of the road only.
      Bikes can use the normal highway on the left side.
     */
    oneway(
      "cycleway:right" -> "lane"
    ) should equal(Both)
  }

  test("M1") {
    oneway(
      "oneway" -> "yes",
      "cycleway" -> "lane",
      "oneway:bicycle" -> "no"
    ) should equal(Both)

    oneway(
      "oneway" -> "yes",
      "cycleway:left" -> "opposite_lane",
      "cycleway:right" -> "lane"
    ) should equal(Both)
  }

  test("M2a") {
    oneway(
      "oneway" -> "yes",
      "cycleway:right" -> "lane"
    ) should equal(Forward)

    oneway(
      "oneway" -> "yes",
      "cycleway" -> "lane"
    ) should equal(Forward)
  }

  test("M2b") {
    oneway(
      "oneway" -> "yes",
      "cycleway:left" -> "lane"
    ) should equal(Both)

    oneway(
      "oneway" -> "yes",
      "cycleway" -> "lane"
    ) should equal(Forward)
  }

  test("M2c") {
    oneway(
      "oneway" -> "yes",
      "lanes" -> "2",
      "cycleway" -> "lane"
    ) should equal(Forward)
  }

  test("M2d") {
    oneway(
      "oneway" -> "yes",
      "cycleway:left" -> "lane",
      "oneway:bicycle" -> "no"
    ) should equal(Both)
  }

  test("M3a") {
    oneway(
      "oneway" -> "yes",
      "oneway:bicycle" -> "no",
      "cycleway:left" -> "opposite_lane"
    ) should equal(Both)

    oneway(
      "oneway" -> "yes",
      "oneway:bicycle" -> "no",
      "cycleway" -> "opposite_lane"
    ) should equal(Both)
  }

  test("M3b") {
    oneway(
      "oneway" -> "yes",
      "oneway:bicycle" -> "no",
      "cycleway:right" -> "opposite_lane"
    ) should equal(Both)

    oneway(
      "oneway" -> "yes",
      "oneway:bicycle" -> "no",
      "cycleway" -> "opposite_lane"
    ) should equal(Both)
  }

  test("T1") {
    oneway(
      "cycleway" -> "track"
    ) should equal(Both)
  }

  test("T2") {
    oneway(
      "cycleway:right" -> "track",
      "oneway:bicycle" -> "no"
    ) should equal(Both)
  }

  test("T3") {
    oneway(
      "oneway" -> "yes",
      "cycleway:right" -> "track",
      "oneway:bicycle" -> "no"
    ) should equal(Both)
  }

  test("T4") {
    /*
      Bikes can use the normal highway on the left side.
     */
    oneway(
      "cycleway:right" -> "track"
    ) should equal(Both)
  }

  test("oneWayTags") {

    val tags = Tags.from(
      "oneway" -> "yes",
      "oneway:bicycle" -> "no",
      "bicycle:oneway" -> "no",
      "junction" -> "roundabout",
      "cycleway:right" -> "lane",
      "othertag" -> "value"
    )

    val oneWayTags = OneWayAnalyzer.oneWayTags(newWay(1, tags = tags))
    oneWayTags.has("oneway", "yes") should equal(true)
    oneWayTags.has("oneway:bicycle", "no") should equal(true)
    oneWayTags.has("bicycle:oneway", "no") should equal(true)
    oneWayTags.has("junction", "roundabout") should equal(true)
    oneWayTags.has("cycleway:right", "lane") should equal(true)

    oneWayTags.has("othertag") should equal(false)
  }

  private def oneway(tags: (String, String)*): WayDirection = {
    new OneWayAnalyzer(newWay(1, tags = Tags.from(tags: _*))).direction
  }
}
