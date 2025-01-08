package kpn.api.custom

import kpn.api.common.NetworkScope
import kpn.api.common.RouteType
import kpn.core.util.UnitTest

class ScopedRouteTypeTest extends UnitTest {

  test("all") {
    ScopedRouteType.all should equal(
      Seq(
        ScopedRouteType(NetworkScope.local, RouteType.hiking, "lwn"),
        ScopedRouteType(NetworkScope.regional, RouteType.hiking, "rwn"),
        ScopedRouteType(NetworkScope.national, RouteType.hiking, "nwn"),
        ScopedRouteType(NetworkScope.international, RouteType.hiking, "iwn"),
        ScopedRouteType(NetworkScope.local, RouteType.cycling, "lcn"),
        ScopedRouteType(NetworkScope.regional, RouteType.cycling, "rcn"),
        ScopedRouteType(NetworkScope.national, RouteType.cycling, "ncn"),
        ScopedRouteType(NetworkScope.international, RouteType.cycling, "icn"),
        ScopedRouteType(NetworkScope.local, RouteType.horseRiding, "lhn"),
        ScopedRouteType(NetworkScope.regional, RouteType.horseRiding, "rhn"),
        ScopedRouteType(NetworkScope.national, RouteType.horseRiding, "nhn"),
        ScopedRouteType(NetworkScope.international, RouteType.horseRiding, "ihn"),
        ScopedRouteType(NetworkScope.local, RouteType.canoe, "lpn"),
        ScopedRouteType(NetworkScope.regional, RouteType.canoe, "rpn"),
        ScopedRouteType(NetworkScope.national, RouteType.canoe, "npn"),
        ScopedRouteType(NetworkScope.international, RouteType.canoe, "ipn"),
        ScopedRouteType(NetworkScope.local, RouteType.motorboat, "lmn"),
        ScopedRouteType(NetworkScope.regional, RouteType.motorboat, "rmn"),
        ScopedRouteType(NetworkScope.national, RouteType.motorboat, "nmn"),
        ScopedRouteType(NetworkScope.international, RouteType.motorboat, "imn"),
        ScopedRouteType(NetworkScope.local, RouteType.inlineSkating, "lin"),
        ScopedRouteType(NetworkScope.regional, RouteType.inlineSkating, "rin"),
        ScopedRouteType(NetworkScope.national, RouteType.inlineSkating, "nin"),
        ScopedRouteType(NetworkScope.international, RouteType.inlineSkating, "iin")
      )
    )
  }
}
