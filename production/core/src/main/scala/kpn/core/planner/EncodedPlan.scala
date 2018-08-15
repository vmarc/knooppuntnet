package kpn.core.planner

trait EncodedPlanItem {
  def encoded: Option[String] = None
}

case class EncodedNode(nodeId: Long) extends EncodedPlanItem {
  override def encoded: Option[String] = Some("N" + nodeId)
}

case class EncodedIntermediateNode(nodeId: Long) extends EncodedPlanItem {
  override def encoded: Option[String] = Some("I" + nodeId)
}

case class EncodedRoute(routeId: Long) extends EncodedPlanItem {
  override def encoded: Option[String] = Some("R" + routeId)
}

case class EncodedMessage(message: PlanMessage) extends EncodedPlanItem

object EncodedPlan {

  def apply(encodedPlanString: String): EncodedPlan = {
    val splitted = encodedPlanString.trim.split("-")
    val items = if (splitted.length == 1 && splitted(0).isEmpty) {
      Seq()
    }
    else {
      splitted.toSeq.map { encodedItem =>
        if (encodedItem.startsWith("N")) {
          EncodedNode(encodedItem.tail.toLong)
        }
        else if (encodedItem.startsWith("I")) {
          EncodedIntermediateNode(encodedItem.tail.toLong)
        }
        else if (encodedItem.startsWith("R")) {
          EncodedRoute(encodedItem.tail.toLong)
        }
        else {
          val message = s"invalid syntax for encoded item '$encodedItem' in encoded plan '$encodedPlanString'"
          throw new EncodedPlanException(message)
        }
      }
    }
    EncodedPlan(items)
  }
}

case class EncodedPlan(items: Seq[EncodedPlanItem]) {

  def userNodeIds: Seq[Long] = items.flatMap {
    case n: EncodedNode => Some(n.nodeId)
    case _ => None
  }

  def allNodeIds: Seq[Long] = items.flatMap {
    case n: EncodedNode => Some(n.nodeId)
    case n: EncodedIntermediateNode => Some(n.nodeId)
    case _ => None
  }

  def routeIds: Seq[Long] = items.flatMap {
    case r: EncodedRoute => Some(r.routeId)
    case _ => None
  }

  def encoded: String = items.flatMap(_.encoded).mkString("-")

  def add(message: PlanMessage): EncodedPlan = {
    EncodedPlan(items ++ Seq(EncodedMessage(message)))
  }

  def add(extraItems: Seq[EncodedPlanItem]): EncodedPlan = {
    EncodedPlan(items ++ extraItems)
  }
}
