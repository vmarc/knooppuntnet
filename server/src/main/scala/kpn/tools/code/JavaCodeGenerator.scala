package kpn.tools.code

import kpn.core.util.Log
import kpn.tools.code.domain.ClassInfo

object JavaCodeGenerator {
  def main(args: Array[String]): Unit = {
    new JavaCodeGenerator().generate()
  }
}

class JavaCodeGenerator {

  private val log = Log(classOf[CodeGenerator])

  def generate(): Unit = {
    val classInfos = ClassInfoReader.collectClassInfos()
    generateJava(classInfos)
  }

  private def generateJava(classInfos: Seq[ClassInfo]): Unit = {
    val apiCLassInfos = classInfos.filter(_.packageName.startsWith("kpn.api.common"))
    new JavaWriter().generate(apiCLassInfos)
  }
}
