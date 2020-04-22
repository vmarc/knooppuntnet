package kpn.core.tools.typescript

import kpn.core.tools.typescript.RelativePathUtil.dependencyRelativePath
import kpn.core.util.UnitTest

class RelativePathUtilTest extends UnitTest {

  test("dependencyRelativePath") {
    dependencyRelativePath("/a/b", "/a/b") should equal(".")
    dependencyRelativePath("/a/b/c/d", "/a/b") should equal("../..")
    dependencyRelativePath("/a/b/c/d", "/a/b/d") should equal("../../d")
    dependencyRelativePath("/a/b", "/a/b/c/d") should equal("./c/d")
  }

}
