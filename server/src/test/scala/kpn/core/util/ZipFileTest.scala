package kpn.core.util

import java.io.FileInputStream
import java.util.zip.ZipInputStream

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ZipFileTest extends AnyFunSuite with Matchers {

  test("write zip file") {
    val filename = "/tmp/test.zip"
    val zip = new ZipFile(filename)
    try {
      zip.add("one.txt", "content of file one")
      zip.add("two.txt", "content of file two")
    }
    finally {
      zip.close()
    }

    val entries = ZipFile.read(filename: String)

    val in = new ZipInputStream(new FileInputStream("/tmp/test.zip"))
    val entry1 = in.getNextEntry
    entry1.getName should equal("one.txt")
    val entry2 = in.getNextEntry
    entry2.getName should equal("two.txt")
    in.close()
  }
}
