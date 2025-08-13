package kpn.tools.code

import kpn.api.common.data.raw.RawNode
import kpn.core.util.UnitTest
import org.scalatest.BeforeAndAfterEach

import scala.reflect.runtime.universe.Mirror
import scala.reflect.runtime.universe.runtimeMirror

class ClazzTest extends UnitTest with BeforeAndAfterEach {

  private var mirror: Mirror = _

  override def beforeEach(): Unit = {
    mirror = runtimeMirror(classOf[RawNode].getClassLoader)
  }

  test("isEnumEntry") {

    val clazz1 = newClazz("kpn.api.common.RouteType$canoe$")
    val clazz2 = newClazz("kpn.api.common.RouteType$")
    val clazz3 = newClazz("kpn.api.common.NodeName")

    assert(clazz1.isEnumEntry)
    assert(!clazz2.isEnumEntry)
    assert(!clazz3.isEnumEntry)
  }

  test("isEnum") {

    val clazz1 = newClazz("kpn.api.common.RouteType$")
    val clazz2 = newClazz("kpn.api.common.RouteType$canoe$")
    val clazz3 = newClazz("kpn.api.common.NodeName")

    assert(clazz1.isEnum)
    assert(!clazz2.isEnum)
    assert(!clazz3.isEnum)
  }

  test("isStorable") {

    val clazz1 = newClazz("kpn.database.base.Id")
    val clazz2 = newClazz("kpn.api.common.NodeName")

    assert(clazz1.isStorable)
    assert(!clazz2.isStorable)
  }

  test("isApi") {

    val clazz1 = newClazz("kpn.api.common.NodeName")
    val clazz2 = newClazz("kpn.database.base.Id")

    assert(clazz1.isApi)
    assert(!clazz2.isApi)
  }

  private def newClazz(fullName: String): Clazz = {
    new Clazz(mirror.staticClass(fullName).typeSignature)
  }
}
