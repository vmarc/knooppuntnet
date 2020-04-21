package kpn.core.util

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GZipFileTest extends AnyFunSuite with Matchers {

  test("write/read gzip") {
    val input = "een\ntwee\ndrie"
    GZipFile.write("/tmp/test.txt.gz", input)
    val string = GZipFile.read("/tmp/test.txt.gz")
    string should equal(input)
  }
}
