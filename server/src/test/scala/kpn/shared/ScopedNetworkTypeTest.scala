package kpn.shared

import org.scalatest.FunSuite
import org.scalatest.Matchers

class ScopedNetworkTypeTest extends FunSuite with Matchers {

  test("all") {
    ScopedNetworkType.all should equal(
      Seq(
        ScopedNetworkType(NetworkScope.local, NetworkType.hiking, "lwn"),
        ScopedNetworkType(NetworkScope.regional, NetworkType.hiking, "rwn"),
        ScopedNetworkType(NetworkScope.national, NetworkType.hiking, "nwn"),
        ScopedNetworkType(NetworkScope.international, NetworkType.hiking, "iwn"),
        ScopedNetworkType(NetworkScope.local, NetworkType.bicycle, "lcn"),
        ScopedNetworkType(NetworkScope.regional, NetworkType.bicycle, "rcn"),
        ScopedNetworkType(NetworkScope.national, NetworkType.bicycle, "ncn"),
        ScopedNetworkType(NetworkScope.international, NetworkType.bicycle, "icn"),
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
        ScopedNetworkType(NetworkScope.local, NetworkType.inlineSkates, "lin"),
        ScopedNetworkType(NetworkScope.regional, NetworkType.inlineSkates, "rin"),
        ScopedNetworkType(NetworkScope.national, NetworkType.inlineSkates, "nin"),
        ScopedNetworkType(NetworkScope.international, NetworkType.inlineSkates, "iin")
      )
    )
  }

}
