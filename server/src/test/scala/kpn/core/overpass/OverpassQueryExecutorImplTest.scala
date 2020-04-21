package kpn.core.overpass

import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class OverpassQueryExecutorImplTest extends FunSuite with Matchers {

  ignore("success: osm3s_query writes query result on stdout and exits with code 0") {
    val simulatedCommand = "echo prompt>/dev/stderr;echo one;echo two;echo three;exit 0"
    execute(simulatedCommand) should equal("one\ntwo\nthree")
  }

  ignore("error: osm3s_query writes a syntax error message on stderr and exits with success code 0") {
    val simulatedCommand = "echo prompt>/dev/stderr;echo syntax error>/dev/stderr;echo one;echo two;echo three;exit 0"
    intercept[RuntimeException] {
      execute(simulatedCommand)
    }.getMessage should equal("Error(s) generated while executing query (exit=0): ([timeout:120];Query) \n  syntax error")
  }

  ignore("error: osm3s_query exits with error code (non-zero code)") {
    val simulatedCommand = "echo prompt>/dev/stderr;echo one;echo two;echo three;exit 1"
    intercept[RuntimeException] {
      execute(simulatedCommand)
    }.getMessage should equal("Error(s) generated while executing query (exit=1): ([timeout:120];Query) ")
  }

  private def execute(simulatedCommand: String): String = {
    val command = Seq("bash", "-c", simulatedCommand)
    new OverpassQueryExecutorImpl().executeQuery(None, MockQuery())
  }

  case class MockQuery(name: String = "name", string: String = "Query") extends OverpassQuery
}
