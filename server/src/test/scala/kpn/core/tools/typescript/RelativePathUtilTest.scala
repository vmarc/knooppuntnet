package kpn.core.tools.typescript

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import kpn.core.tools.typescript.RelativePathUtil.dependencyRelativePath

class RelativePathUtilTest extends AnyFunSuite with Matchers {

  test("dependencyRelativePath") {
    dependencyRelativePath("/a/b", "/a/b") should equal(".")
    dependencyRelativePath("/a/b/c/d", "/a/b") should equal("../..")
    dependencyRelativePath("/a/b/c/d", "/a/b/d") should equal("../../d")
    dependencyRelativePath("/a/b", "/a/b/c/d") should equal("./c/d")
  }

}
