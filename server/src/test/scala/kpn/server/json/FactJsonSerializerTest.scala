package kpn.server.json

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.shared.Fact
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.test.context.TestContextManager

@JsonTest
class FactJsonSerializerTest extends FunSuite with Matchers {

  @Autowired
  var objectMapper: ObjectMapper = _

  new TestContextManager(this.getClass).prepareTestInstance(this)

  test("serializer") {
    objectMapper.writeValueAsString(Fact.Added) should equal(""""Added"""")
  }
}
