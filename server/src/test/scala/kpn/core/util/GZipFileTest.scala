package kpn.core.util

class GZipFileTest extends UnitTest {

  test("write/read gzip") {
    val input = "een\ntwee\ndrie"
    GZipFile.write("/tmp/test.txt.gz", input)
    val string = GZipFile.read("/tmp/test.txt.gz")
    string should equal(input)
  }
}
