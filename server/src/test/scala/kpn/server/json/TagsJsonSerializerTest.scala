package kpn.server.json

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.server.ServerApplication
import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestContextManager

@ContextConfiguration(classes = Array(classOf[ServerApplication]))
class TagsJsonSerializerTest extends FunSuite with Matchers {

  @Autowired
  var objectMapper: ObjectMapper = _

  new TestContextManager(this.getClass).prepareTestInstance(this)

  test("serializer") {
    val tags = Tags.from("key1" -> "value1", "key2" -> "value2", "key3" -> "value3")
    val json = objectMapper.writeValueAsString(tags)
    json should equal("""[["key1","value1"],["key2","value2"],["key3","value3"]]""")
  }
}
