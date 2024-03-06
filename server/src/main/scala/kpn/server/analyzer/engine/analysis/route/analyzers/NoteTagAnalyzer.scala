package kpn.server.analyzer.engine.analysis.route.analyzers

object NoteTagAnalyzer {
  def isDeprecatedNoteTag(tagValue: String): Boolean = {
    if (tagValue.contains("-")) {
      val parts = tagValue.split(" - ")
      if (parts.length == 2) {
        isAcceptedNodeName(parts.head) && isAcceptedNodeName(parts.last)
      }
      else {
        val parts2 = tagValue.split("-")
        if (parts2.length == 2) {
          isAcceptedNodeName(parts2.head) && isAcceptedNodeName(parts2.last)
        }
        else {
          false
        }
      }
    }
    else {
      false
    }
  }

  private def isAcceptedNodeName(name: String): Boolean = {
    val trimmedName = name.trim()
    // StringUtils.isNumeric(trimmedName) || trimmedName == "?" || trimmedName == "START"
    trimmedName.length > 0
  }
}
