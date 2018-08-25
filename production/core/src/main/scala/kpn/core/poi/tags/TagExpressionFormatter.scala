package kpn.core.poi.tags

class TagExpressionFormatter {

  def format(expression: TagExpression): String = {

    expression match {

      case e: And => format(e.left) + format(e.right)

      case e: HasTag =>
        if (e.allowedValues.isEmpty) {
          s"[${e.tagKey}]"
        }
        else if (e.allowedValues.size == 1) {
          s"[${e.tagKey}=${e.allowedValues.head}]"
        }
        else {
          val values = e.allowedValues.mkString("|")
          s"[${e.tagKey}~'$values']"
        }

      case e: NotHasTag =>
        if (e.allowedValues.isEmpty) {
          s"[${e.tagKey}!~'.']"
        }
        else {
          val values = e.allowedValues.mkString("|")
          s"[${e.tagKey}!~'$values']"
        }

      case _ => throw new IllegalStateException("Unexpected expression: " + expression)
    }
  }

}
