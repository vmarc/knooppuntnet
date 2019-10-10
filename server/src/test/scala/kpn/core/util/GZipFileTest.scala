package kpn.core.util

import org.scalatest.FunSuite
import org.scalatest.Matchers

class GZipFileTest extends FunSuite with Matchers {

  test("write/read gzip") {
    val input = "een\ntwee\ndrie"
    GZipFile.write("/tmp/test.txt.gz", input)
    val string = GZipFile.read("/tmp/test.txt.gz")
    string should equal(input)
  }
}
