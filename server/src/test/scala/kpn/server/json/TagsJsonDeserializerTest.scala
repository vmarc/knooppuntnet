package kpn.server.json

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.test.context.TestContextManager

@JsonTest
class TagsJsonDeserializerTest extends FunSuite with Matchers {

  @Autowired
  var objectMapper: ObjectMapper = _

  new TestContextManager(this.getClass).prepareTestInstance(this)

  test("deserializer") {
    val tags = objectMapper.readValue("""[["key1","value1"],["key2","value2"],["key3","value3"]]""", classOf[Tags])
    tags should equal(Tags.from("key1" -> "value1", "key2" -> "value2", "key3" -> "value3"))
  }
}
