package kpn.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.server.ServerApplication
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestContextManager

@ContextConfiguration(classes = Array(classOf[ServerApplication]))
class JsonConfigurationTest extends FunSuite with Matchers {

  @Autowired
  var objectMapper: ObjectMapper = _

  new TestContextManager(this.getClass).prepareTestInstance(this)

  test("case class json") {
    val example = JsonExample("John Doe", 123)
    val json = objectMapper.writeValueAsString(example)
    json should equal("""{"name":"John Doe","age":123}""")
    objectMapper.readValue(json, classOf[JsonExample]) should equal(example)
  }
}
