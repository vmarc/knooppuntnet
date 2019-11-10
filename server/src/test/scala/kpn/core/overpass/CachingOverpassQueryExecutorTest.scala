package kpn.core.overpass

import java.io.File

import kpn.api.custom.Timestamp
import kpn.core.util.GZipFile
import org.apache.commons.io.FileUtils
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class CachingOverpassQueryExecutorTest extends FunSuite with Matchers with MockFactory {

  test("test caching") {
    val cacheRootDir = new File("/tmp/test-cache")
    if (cacheRootDir.exists()) {
      FileUtils.forceDelete(cacheRootDir)
    }
    try {
      val executor = stub[OverpassQueryExecutor]
      (executor.execute _).when(*).returns("result")

      val cachingExecutor = new CachingOverpassQueryExecutor(cacheRootDir, executor)
      cachingExecutor.executeQuery(Some(Timestamp(2015, 11, 8, 12, 13, 14)), QueryNode(1))

      val contents = GZipFile.read("/tmp/test-cache/2015/11/08/12/13/14/node-1.xml.gz")
      contents should equal("result")
    }
    finally {
      FileUtils.forceDelete(cacheRootDir)
    }
  }
}
