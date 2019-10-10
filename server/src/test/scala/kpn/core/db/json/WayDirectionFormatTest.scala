package kpn.core.db.json

import kpn.core.common.Time
import kpn.core.db.json.JsonFormats._
import kpn.shared.data.Tags
import kpn.shared.route.Both
import kpn.shared.route.RouteMemberInfo
import org.scalatest.FunSuite
import org.scalatest.Matchers

class WayDirectionFormatTest extends FunSuite with Matchers {

  test("WayDirection") {
    val routeMemberInfo = RouteMemberInfo(
      1,
      "node",
      isWay = false,
      Seq(),
      "",
      "",
      0,
      "",
      1,
      "connection",
      Time.now,
      isAccessible = true,
      "100",
      "",
      "description",
      Both,
      Tags.empty
    )

    val json = routeMemberInfoFormat.write(routeMemberInfo)
    routeMemberInfoFormat.read(json) should equal(routeMemberInfo)
  }
}
