package kpn.tools.code

import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Timestamp
import kpn.core.util.UnitTest
import kpn.tools.code.domain.ClassField
import kpn.tools.code.domain.ClassType
import org.scalatest.BeforeAndAfterEach

import scala.reflect.runtime.universe.Mirror
import scala.reflect.runtime.universe.runtimeMirror

case class TestClass(
  id: Long,
  version: Int,
  name: String,
  value: Double,
  timestamp1: Timestamp,
  timestamp2: Timestamp,
  flagged: Option[Boolean],
  options: Seq[String],
  map: Map[String, Timestamp],
  timestamps: Option[Seq[Timestamp]],
  selfRef: TestClass
)

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

  test("fields") {

    val clazz = newClazz("kpn.tools.code.TestClass")

    assertEqual(
      clazz.fields,
      Seq(
        ClassField(
          "id",
          ClassType(typeName = Some("Long"))
        ),
        ClassField(
          "version",
          ClassType(typeName = Some("Int"))
        ),
        ClassField(
          "name",
          ClassType(typeName = Some("String"))
        ),
        ClassField(
          "value",
          ClassType(typeName = Some("Double"))
        ),
        ClassField(
          "timestamp1",
          ClassType(
            typeName = Some("Timestamp"),
            packageName = Some("kpn.api.custom"),
          )
        ),
        ClassField(
          "timestamp2",
          ClassType(
            typeName = Some("Timestamp"),
            packageName = Some("kpn.api.custom"),
          )
        ),
        ClassField(
          "flagged",
          ClassType(
            typeName = Some("Boolean"),
          )
        ),
        ClassField(
          "options",
          ClassType(
            arrayType = Some(
              ClassType(
                Some("String"),
              )
            ),
            arrayTypeClass = Some("Seq"),
          )
        ),
        ClassField(
          "map",
          ClassType(
            mapTypes = Some(
              (
                ClassType(
                  Some("String")
                ),
                ClassType(
                  typeName = Some("Timestamp"),
                  packageName = Some("kpn.api.custom"),
                )
              )
            )
          )
        ),
        ClassField(
          "timestamps",
          ClassType(
            arrayType = Some(
              ClassType(
                typeName = Some("Timestamp"),
                packageName = Some("kpn.api.custom"),
                optional = true
              )
            ),
            arrayTypeClass = Some("Seq"),
            optional = true
          ),
        ),
        ClassField(
          "selfRef",
          ClassType(
            typeName = Some("TestClass"),
            packageName = Some("kpn.tools.code"),
          )
        )
      )
    )
  }

  private def newClazz(fullName: String): Clazz = {
    new Clazz(mirror.staticClass(fullName).typeSignature)
  }
}
