package kpn.api.custom

import kpn.core.util.UnitTest

class ScopedNetworkTypeTest extends UnitTest {

  test("all") {
    ScopedNetworkType.all should equal(
      Seq(
        ScopedNetworkType(NetworkScope.local, NetworkType.hiking, "lwn"),
        ScopedNetworkType(NetworkScope.regional, NetworkType.hiking, "rwn"),
        ScopedNetworkType(NetworkScope.national, NetworkType.hiking, "nwn"),
        ScopedNetworkType(NetworkScope.international, NetworkType.hiking, "iwn"),
        ScopedNetworkType(NetworkScope.local, NetworkType.cycling, "lcn"),
        ScopedNetworkType(NetworkScope.regional, NetworkType.cycling, "rcn"),
        ScopedNetworkType(NetworkScope.national, NetworkType.cycling, "ncn"),
        ScopedNetworkType(NetworkScope.international, NetworkType.cycling, "icn"),
        ScopedNetworkType(NetworkScope.local, NetworkType.horseRiding, "lhn"),
        ScopedNetworkType(NetworkScope.regional, NetworkType.horseRiding, "rhn"),
        ScopedNetworkType(NetworkScope.national, NetworkType.horseRiding, "nhn"),
        ScopedNetworkType(NetworkScope.international, NetworkType.horseRiding, "ihn"),
        ScopedNetworkType(NetworkScope.local, NetworkType.canoe, "lpn"),
        ScopedNetworkType(NetworkScope.regional, NetworkType.canoe, "rpn"),
        ScopedNetworkType(NetworkScope.national, NetworkType.canoe, "npn"),
        ScopedNetworkType(NetworkScope.international, NetworkType.canoe, "ipn"),
        ScopedNetworkType(NetworkScope.local, NetworkType.motorboat, "lmn"),
        ScopedNetworkType(NetworkScope.regional, NetworkType.motorboat, "rmn"),
        ScopedNetworkType(NetworkScope.national, NetworkType.motorboat, "nmn"),
        ScopedNetworkType(NetworkScope.international, NetworkType.motorboat, "imn"),
        ScopedNetworkType(NetworkScope.local, NetworkType.inlineSkating, "lin"),
        ScopedNetworkType(NetworkScope.regional, NetworkType.inlineSkating, "rin"),
        ScopedNetworkType(NetworkScope.national, NetworkType.inlineSkating, "nin"),
        ScopedNetworkType(NetworkScope.international, NetworkType.inlineSkating, "iin")
      )
    )
  }

}
