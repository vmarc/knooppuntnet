package kpn.server.analyzer.engine.analysis.route.structure

case class ReferenceWayInfo(
  isLinkedToPreviousMember: Boolean,
  isLinkedToNextMember: Boolean,
  isLoop: Boolean,
  isOnewayLoopForwardPart: Boolean,
  isOnewayLoopBackwardPart: Boolean,
  isOnewayHead: Boolean,
  isOnewayTail: Boolean,
  direction: ReferenceDirection
) {

  def reportString: String = {
    val sb = new StringBuilder
    sb.append("   p " + bool(isLinkedToPreviousMember))
    sb.append("   n " + bool(isLinkedToNextMember))
    sb.append("   loop " + bool(isLoop))
    sb.append("   fp " + bool(isOnewayLoopForwardPart))
    sb.append("   bp " + bool(isOnewayLoopBackwardPart))
    sb.append("   head " + bool(isOnewayHead))
    sb.append("   tail " + bool(isOnewayTail))
    sb.append(String.format("   d %s", direction.toString.toLowerCase))
    sb.toString
  }

  def name: String = {
    //    if (!isValid) {
    //      "n"
    //    }
    //    else {

    val linkTypeLetter = direction match {
      case ReferenceDirection.Forward => "f"
      case ReferenceDirection.Backward => "b"
      case ReferenceDirection.RoundaboutLeft => "r"
      case ReferenceDirection.RoundaboutRight => "r"
      case ReferenceDirection.Unknown => "n"
    }

    val code = (if (isLinkedToPreviousMember) 1 else 0) +
      (if (isLinkedToNextMember) 2 else 0) +
      (if (isLoop) 4 else 0) +
      (if (isOnewayLoopForwardPart) 8 else 0) +
      (if (isOnewayLoopBackwardPart) 16 else 0) +
      (if (isOnewayHead) 32 else 0) +
      (if (isOnewayTail) 64 else 0)

    "w%s%03d".format(linkTypeLetter, code)
  }

  private def bool(value: Boolean): String = {
    if (value) "■" else " "
  }
}
