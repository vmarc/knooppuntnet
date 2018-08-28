package kpn.core.poi.tags

class TagExpressionFormatter {

  def format(expression: TagExpression): String = {

    expression match {
      case e: And => format(e.left) + format(e.right)
      case e: HasTag => formatHasTag(e)
      case e: NotHasTag => formatNotHasTag(e)
      case e: TagContains => formatTagContains(e)
      case e: NotTagContains => formatNotTagContains(e)
      case _ => throw new IllegalStateException("Unexpected expression: " + expression)
    }
  }

  private def formatHasTag(hasTag: HasTag): String = {
    if (hasTag.allowedValues.isEmpty) {
      s"[${hasTag.tagKey}]"
    }
    else if (hasTag.allowedValues.size == 1) {
      s"[${hasTag.tagKey}=${hasTag.allowedValues.head}]"
    }
    else {
      val values = hasTag.allowedValues.map(v => s"^$v$$").mkString("|")
      s"[${hasTag.tagKey}~'$values']"
    }
  }

  private def formatNotHasTag(notHasTag: NotHasTag): String = {
    if (notHasTag.allowedValues.isEmpty) {
      s"[${notHasTag.tagKey}!~'.']"
    }
    else if (notHasTag.allowedValues.size == 1) {
      s"[${notHasTag.tagKey}!=${notHasTag.allowedValues.head}]"
    }
    else {
      throw new IllegalStateException("Unexpected expression (at most 1 value): " + notHasTag)
    }
  }

  private def formatTagContains(tagContains: TagContains): String = {
    if (tagContains.tagValues.isEmpty) {
      throw new IllegalStateException("Unexpected expression (at least 1 value): " + tagContains)
    }
    else {
      val values = tagContains.tagValues.map(v => s"$v").mkString("|")
      s"[${tagContains.tagKey}~'$values']"
    }
  }

  private def formatNotTagContains(notTagContains: NotTagContains): String = {
    if (notTagContains.tagValues.isEmpty) {
      throw new IllegalStateException("Unexpected expression (at least 1 value): " + notTagContains)
    }
    else {
      val values = notTagContains.tagValues.map(v => s"$v").mkString("|")
      s"[${notTagContains.tagKey}!~'$values']"
    }
  }

}
