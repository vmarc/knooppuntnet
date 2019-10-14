package kpn.server

import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestContextManager

@ContextConfiguration(classes = Array(classOf[ServerApplication]))
class ServerApplicationTests extends FunSuite with Matchers {

  new TestContextManager(this.getClass).prepareTestInstance(this)

  test("context loads") {
  }
}
