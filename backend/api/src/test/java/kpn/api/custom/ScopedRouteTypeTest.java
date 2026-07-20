package kpn.api.custom;

import kpn.api.common.RouteScope;
import kpn.api.common.RouteType;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScopedRouteTypeTest {

  @Test
  public void all() {
    assertEquals(
      ImmutableList.of(
        new ScopedRouteType(RouteType.HIKING, RouteScope.LOCAL, "lwn"),
        new ScopedRouteType(RouteType.HIKING, RouteScope.REGIONAL, "rwn"),
        new ScopedRouteType(RouteType.HIKING, RouteScope.NATIONAL, "nwn"),
        new ScopedRouteType(RouteType.HIKING, RouteScope.INTERNATIONAL, "iwn"),
        new ScopedRouteType(RouteType.CYCLING, RouteScope.LOCAL, "lcn"),
        new ScopedRouteType(RouteType.CYCLING, RouteScope.REGIONAL, "rcn"),
        new ScopedRouteType(RouteType.CYCLING, RouteScope.NATIONAL, "ncn"),
        new ScopedRouteType(RouteType.CYCLING, RouteScope.INTERNATIONAL, "icn"),
        new ScopedRouteType(RouteType.HORSE_RIDING, RouteScope.LOCAL, "lhn"),
        new ScopedRouteType(RouteType.HORSE_RIDING, RouteScope.REGIONAL, "rhn"),
        new ScopedRouteType(RouteType.HORSE_RIDING, RouteScope.NATIONAL, "nhn"),
        new ScopedRouteType(RouteType.HORSE_RIDING, RouteScope.INTERNATIONAL, "ihn"),
        new ScopedRouteType(RouteType.CANOE, RouteScope.LOCAL, "lpn"),
        new ScopedRouteType(RouteType.CANOE, RouteScope.REGIONAL, "rpn"),
        new ScopedRouteType(RouteType.CANOE, RouteScope.NATIONAL, "npn"),
        new ScopedRouteType(RouteType.CANOE, RouteScope.INTERNATIONAL, "ipn"),
        new ScopedRouteType(RouteType.MOTORBOAT, RouteScope.LOCAL, "lmn"),
        new ScopedRouteType(RouteType.MOTORBOAT, RouteScope.REGIONAL, "rmn"),
        new ScopedRouteType(RouteType.MOTORBOAT, RouteScope.NATIONAL, "nmn"),
        new ScopedRouteType(RouteType.MOTORBOAT, RouteScope.INTERNATIONAL, "imn"),
        new ScopedRouteType(RouteType.INLINE_SKATING, RouteScope.LOCAL, "lin"),
        new ScopedRouteType(RouteType.INLINE_SKATING, RouteScope.REGIONAL, "rin"),
        new ScopedRouteType(RouteType.INLINE_SKATING, RouteScope.NATIONAL, "nin"),
        new ScopedRouteType(RouteType.INLINE_SKATING, RouteScope.INTERNATIONAL, "iin"),
        new ScopedRouteType(RouteType.MTB, RouteScope.LOCAL, "l?n"),
        new ScopedRouteType(RouteType.MTB, RouteScope.REGIONAL, "r?n"),
        new ScopedRouteType(RouteType.MTB, RouteScope.NATIONAL, "n?n"),
        new ScopedRouteType(RouteType.MTB, RouteScope.INTERNATIONAL, "i?n")
      ),
      ScopedRouteType.all()
    );
  }
}
