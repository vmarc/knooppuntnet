package kpn.core.tools.typescript

object RelativePathUtil {

  def dependencyRelativePath(caseClassPath: String, dependencyPath: String): String = {
    val r = new java.io.File(caseClassPath).toPath.relativize(new java.io.File(dependencyPath).toPath).toString

    if (r.isEmpty) {
      "."
    }
    else if (r.startsWith("..")) {
      r
    }
    else {
      "./" + r
    }


  }

}
