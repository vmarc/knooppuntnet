package kpn.server.json

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.server.ServerApplication
import kpn.shared.Timestamp
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestContextManager

@ContextConfiguration(classes = Array(classOf[ServerApplication]))
class TimestampJsonDeserializerTest extends FunSuite with Matchers {

  @Autowired
  var objectMapper: ObjectMapper = _

  new TestContextManager(this.getClass).prepareTestInstance(this)

  test("deserializer") {
    val timestamp = objectMapper.readValue(""""2018-08-11T12:34:56Z"""", classOf[Timestamp])
    timestamp should equal(Timestamp(2018, 8, 11, 12, 34, 56))
  }
}
