package kpn.tools.code

import kpn.tools.code.domain.ClassInfo

import scala.io.Source

object RootDocReader {
  val scalaCaseClassReader = new ScalaCaseClassReader()

  def main(args: Array[String]): Unit = {
    val rootClassIds = readClassIds()
    val rootClassInfos = rootClassIds.map(classId => scalaCaseClassReader.read(classId))

    println(rootClassInfos)

    val found = rootClassInfos.map(classInfo => classInfo.key -> classInfo).toMap

    val all = rootClassInfos.flatMap { classInfo =>
      collectDependencies(found, classInfo)
    }

    all.foreach(classInfo =>
      println(classInfo.className)
    )
  }

  private def collectDependencies(found: Map[String, ClassInfo], info: ClassInfo): Seq[ClassInfo] = {
    println(info.className)
    val missingClassIds = info.dependencies.filter(classId => !found.contains(classId.key))
    val classInfos = missingClassIds.map(classId => scalaCaseClassReader.read(classId))
    val newFound = found ++ classInfos.map(classInfo => classInfo.key -> classInfo).toMap
    classInfos.flatMap { classInfo =>
      println(s"  ${classInfo.className}")
      collectDependencies(newFound, classInfo)
    }
  }

  private def readClassIds(): Seq[ClassId] = {
    val filename = "src/main/scala/kpn/database/base/Database.scala"
    val source = Source.fromFile(filename)
    try {
      val lines = source.getLines().toSeq

      val collectionPattern = """.*DatabaseCollection\[(\w+)\]""".r

      //   def baseNetworks: DatabaseCollection[BaseNetworkDoc]

      val excludedNames = Seq("WithStringId")

      val classNames = lines
        .flatMap(collectionPattern.findFirstMatchIn)
        .map(_.group(1))
        .filterNot(excludedNames.contains)
        .sorted
        .distinct

      val importLines = lines.filter(_.startsWith("import "))
      val classIds = classNames.map { className =>
        val pattern = s".*\\.$className$$".r
        val packageLine = importLines.find(pattern.matches)
        packageLine match {
          case None => throw new RuntimeException(s"cannot find import for $className in $filename")
          case Some(line) =>
            val packageName = line.dropRight(className.length + 1).drop("import ".length)
            ClassId(className, packageName)
        }
      }

      classIds
    }
    finally {
      source.close()
    }
  }
}
