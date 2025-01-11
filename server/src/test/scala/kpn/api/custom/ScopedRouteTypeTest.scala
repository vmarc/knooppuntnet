package kpn.api.custom

import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.core.util.UnitTest

class ScopedRouteTypeTest extends UnitTest {

  test("all") {
    ScopedRouteType.all should equal(
      Seq(
        ScopedRouteType(RouteType.hiking, RouteScope.local, "lwn"),
        ScopedRouteType(RouteType.hiking, RouteScope.regional, "rwn"),
        ScopedRouteType(RouteType.hiking, RouteScope.national, "nwn"),
        ScopedRouteType(RouteType.hiking, RouteScope.international, "iwn"),
        ScopedRouteType(RouteType.cycling, RouteScope.local, "lcn"),
        ScopedRouteType(RouteType.cycling, RouteScope.regional, "rcn"),
        ScopedRouteType(RouteType.cycling, RouteScope.national, "ncn"),
        ScopedRouteType(RouteType.cycling, RouteScope.international, "icn"),
        ScopedRouteType(RouteType.horseRiding, RouteScope.local, "lhn"),
        ScopedRouteType(RouteType.horseRiding, RouteScope.regional, "rhn"),
        ScopedRouteType(RouteType.horseRiding, RouteScope.national, "nhn"),
        ScopedRouteType(RouteType.horseRiding, RouteScope.international, "ihn"),
        ScopedRouteType(RouteType.canoe, RouteScope.local, "lpn"),
        ScopedRouteType(RouteType.canoe, RouteScope.regional, "rpn"),
        ScopedRouteType(RouteType.canoe, RouteScope.national, "npn"),
        ScopedRouteType(RouteType.canoe, RouteScope.international, "ipn"),
        ScopedRouteType(RouteType.motorboat, RouteScope.local, "lmn"),
        ScopedRouteType(RouteType.motorboat, RouteScope.regional, "rmn"),
        ScopedRouteType(RouteType.motorboat, RouteScope.national, "nmn"),
        ScopedRouteType(RouteType.motorboat, RouteScope.international, "imn"),
        ScopedRouteType(RouteType.inlineSkating, RouteScope.local, "lin"),
        ScopedRouteType(RouteType.inlineSkating, RouteScope.regional, "rin"),
        ScopedRouteType(RouteType.inlineSkating, RouteScope.national, "nin"),
        ScopedRouteType(RouteType.inlineSkating, RouteScope.international, "iin")
      )
    )
  }
}
