package kpn.core.tools.typescript

import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Timestamp
import kpn.core.util.UnitTest

import scala.reflect.runtime.universe._

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
    classInfo.displayString should equal(
      ClassInfo(
        "TestClass",
        "kpn/core/tools/typescript/test-class.ts",
        Seq(
          ClassField(
            "id",
            ClassType("number", primitive = true)
          ),
          ClassField(
            "version",
            ClassType("number", primitive = true)
          ),
          ClassField(
            "name",
            ClassType("string", primitive = true)
          ),
          ClassField(
            "value",
            ClassType("number", primitive = true)
          ),
          ClassField(
            "timestamp1",
            ClassType("Timestamp")
          ),
          ClassField(
            "timestamp2",
            ClassType("Timestamp")
          ),
          ClassField(
            "flagged",
            ClassType("boolean", primitive = true)
          ),
          ClassField(
            "options",
            ClassType(
              "List<string>",
              arrayType = Some(ClassType("string", primitive = true))
            )
          ),
          ClassField(
            "map",
            ClassType(
              "Map<string, Timestamp>",
              mapTypes = Some((ClassType("string", primitive = true), ClassType("Timestamp")))
            )
          ),
          ClassField(
            "timestamps",
            ClassType(
              "List<Timestamp>",
              arrayType = Some(ClassType("Timestamp"))
            )
          ),
          ClassField(
            "selfRef",
            ClassType("TestClass")
          )
        ),
        Seq(
          ClassDependency(
            "Timestamp",
            "../../../api/custom/timestamp"
          )
        )
      ).displayString
    )
  }

}
