package kpn.api.common.route

import scala.collection.mutable

case class Link(
  direction: LinkDirection,
  hasPrev: Boolean,
  hasNext: Boolean,
  isLoop: Boolean,
  isOnewayLoopForwardPart: Boolean,
  isOnewayLoopBackwardPart: Boolean,
  isOnewayHead: Boolean,
  isOnewayTail: Boolean,
) {

  def isBidirectional: Boolean = {
    !isOnewayLoopForwardPart && !isOnewayLoopBackwardPart
  }

  def name: String = {
    val directionLetter = direction match {
      case LinkDirection.Forward => "f"
      case LinkDirection.Backward => "b"
      case LinkDirection.RoundaboutLeft => "r"
      case LinkDirection.RoundaboutRight => "r"
      case LinkDirection.Unconnected => "n"
    }

    val code = (if (hasPrev) 1 else 0) +
      (if (hasNext) 2 else 0) +
      (if (isLoop) 4 else 0) +
      (if (isOnewayLoopForwardPart) 8 else 0) +
      (if (isOnewayLoopBackwardPart) 16 else 0) +
      (if (isOnewayHead) 32 else 0) +
      (if (isOnewayTail) 64 else 0)

    f"w$directionLetter$code%03d"
  }

  def description: String = {
    val sb = new mutable.StringBuilder

    if (!hasPrev) {
      sb.append("*")
    }

    val elements = (if (isLoop) Seq("loop") else Seq.empty) ++
      (if (isOnewayLoopForwardPart) Seq("fp") else Seq.empty) ++
      (if (isOnewayLoopBackwardPart) Seq("bp") else Seq.empty) ++
      (if (isOnewayHead) Seq("head") else Seq.empty) ++
      (if (isOnewayTail) Seq("tail") else Seq.empty) ++
      Seq(direction.toString.toLowerCase)

    sb.append(elements.mkString("-"))

    if (!hasNext) {
      sb.append("*")
    }
    sb.toString()
  }

  def reportString: String = {
    val sb = new StringBuilder
    sb.append(s"p ${bool(hasPrev)}")
    sb.append(s"   n ${bool(hasNext)}")
    sb.append(s"   loop ${bool(isLoop)}")
    sb.append(s"   fp ${bool(isOnewayLoopForwardPart)}")
    sb.append(s"   bp ${bool(isOnewayLoopBackwardPart)}")
    sb.append(s"   head ${bool(isOnewayHead)}")
    sb.append(s"   tail ${bool(isOnewayTail)}")
    sb.append(s"   d ${direction.entryName.toLowerCase}")
    sb.toString
  }

  private def bool(value: Boolean): String = {
    if (value) "■" else " "
  }
}
