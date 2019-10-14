package kpn.server.json

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.server.ServerApplication
import kpn.shared.NetworkType
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestContextManager

@ContextConfiguration(classes = Array(classOf[ServerApplication]))
class NetworkTypeJsonSerializerTest extends FunSuite with Matchers {

  @Autowired
  var objectMapper: ObjectMapper = _

  new TestContextManager(this.getClass).prepareTestInstance(this)

  test("serializer") {
    objectMapper.writeValueAsString(NetworkType.bicycle) should equal(""""rcn"""")
  }
}
