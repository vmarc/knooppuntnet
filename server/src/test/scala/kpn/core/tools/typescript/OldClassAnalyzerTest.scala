package kpn.core.tools.typescript

import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Timestamp
import kpn.core.util.UnitTest

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

class ClassAnalyzerTest extends UnitTest {

  private val mirror = runtimeMirror(classOf[RawNode].getClassLoader)

  test("TestClass") {

    val caseClass = mirror.staticClass("kpn.core.tools.typescript.TestClass").typeSignature
    val classInfo = new ClassAnalyzer().analyze(caseClass)
    assertEqual(
      classInfo,
      OldClassInfo(
        "TestClass",
        Seq(
          OldClassField(
            "id",
            OldClassType("number", primitive = true)
          ),
          OldClassField(
            "version",
            OldClassType("number", primitive = true)
          ),
          OldClassField(
            "name",
            OldClassType("string", primitive = true)
          ),
          OldClassField(
            "value",
            OldClassType("number", primitive = true)
          ),
          OldClassField(
            "timestamp1",
            OldClassType("Timestamp")
          ),
          OldClassField(
            "timestamp2",
            OldClassType("Timestamp")
          ),
          OldClassField(
            "flagged",
            OldClassType("boolean", primitive = true)
          ),
          OldClassField(
            "options",
            OldClassType(
              "Array<string>",
              arrayType = Some(OldClassType("string", primitive = true))
            )
          ),
          OldClassField(
            "map",
            OldClassType(
              "Map<string, Timestamp>",
              mapTypes = Some((OldClassType("string", primitive = true), OldClassType("Timestamp")))
            )
          ),
          OldClassField(
            "timestamps",
            OldClassType(
              "Array<Timestamp>",
              arrayType = Some(OldClassType("Timestamp", optional = true)),
              optional = true
            ),
          ),
          OldClassField(
            "selfRef",
            OldClassType("TestClass")
          )
        ),
        Seq(
          OldClassDependency(
            "Timestamp",
            "@api/custom/timestamp",
          )
        ),
        formClass = false
      )
    )
  }
}
