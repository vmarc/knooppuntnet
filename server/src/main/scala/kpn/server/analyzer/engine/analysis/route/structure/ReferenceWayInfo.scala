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

  private def bool(value: Boolean): String = {
    if (value) "■" else " "
  }
}
