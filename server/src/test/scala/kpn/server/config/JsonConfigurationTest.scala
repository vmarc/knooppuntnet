package kpn.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.scalatest.FunSuite
import org.scalatest.Matchers

class JsonConfigurationTest extends FunSuite with Matchers {

  test("case class json") {
    val objectMapper = new ObjectMapper()
    objectMapper.registerModule(DefaultScalaModule)
    val example = JsonExample("John Doe", 123)
    val json = objectMapper.writeValueAsString(example)
    json should equal("""{"name":"John Doe","age":123}""")
    objectMapper.readValue(json, classOf[JsonExample]) should equal(example)
  }
}
