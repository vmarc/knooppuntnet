package kpn.core.analysis

case class Link(
  linkType: LinkType.Value,
  hasPrev: Boolean,
  hasNext: Boolean,
  isLoop: Boolean,
  isOnewayLoopForwardPart: Boolean,
  isOnewayLoopBackwardPart: Boolean,
  isOnewayHead: Boolean,
  isOnewayTail: Boolean,
  invalid: Boolean
) {

  def isValid: Boolean = !invalid

  def toolTip: String = {
    if (!isValid) {
      ""
    }
    else if (hasPrev && hasNext) {
      "way is connected"
    }
    else if (hasPrev) {
      "way is connected to previous relation member"
    }
    else if (hasNext) {
      "way is connected to next relation member"
    }
    else {
      "way is not connected to previous or next relation member"
    }
  }

  def name: String = {
    if (!isValid) {
      "n"
    }
    else {

      import kpn.core.analysis.LinkType._

      val linkTypeLetter = linkType match {
        case FORWARD => "f"
        case BACKWARD => "b"
        case ROUNDABOUT => "r"
        case NONE => "n"
      }

      val code = (if (hasPrev) 1 else 0) +
        (if (hasNext) 2 else 0) +
        (if (isLoop) 4 else 0) +
        (if (isOnewayLoopForwardPart) 8 else 0) +
        (if (isOnewayLoopBackwardPart) 16 else 0) +
        (if (isOnewayHead) 32 else 0) +
        (if (isOnewayTail) 64 else 0)

      "w%s%03d".format(linkTypeLetter, code)
    }
  }

  def description: String = {
    if (!isValid) {
      "I"
    }
    else {
      val sb = new StringBuilder

      if (!hasPrev) {
        sb.append("*")
      }

      val elements = (if (isLoop) Seq("loop") else Seq()) ++
        (if (isOnewayLoopForwardPart) Seq("fp") else Seq()) ++
        (if (isOnewayLoopBackwardPart) Seq("bp") else Seq()) ++
        (if (isOnewayHead) Seq("head") else Seq()) ++
        (if (isOnewayTail) Seq("tail") else Seq()) ++
        Seq(linkType.toString.toLowerCase)

      sb.append(elements.mkString("-"))

      if (!hasNext) {
        sb.append("*")
      }
      sb.toString()
    }
  }
}
