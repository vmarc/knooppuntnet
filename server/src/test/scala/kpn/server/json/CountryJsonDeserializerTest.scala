package kpn.server.json

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.shared.Country
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.test.context.TestContextManager

@JsonTest
class CountryJsonDeserializerTest extends FunSuite with Matchers {

  @Autowired
  var objectMapper: ObjectMapper = _

  new TestContextManager(this.getClass).prepareTestInstance(this)

  test("deserializer") {
    val country = objectMapper.readValue(""""be"""", classOf[Country])
    country should equal(Country.be)
  }
}
