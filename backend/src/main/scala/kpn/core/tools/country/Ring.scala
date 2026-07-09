package kpn.core.tools.country

object Ring {
  def apply(): Ring = Ring(Seq.empty)
  def apply(way: SkeletonWay): Ring = Ring(Seq(way))
}

case class Ring(ways: Seq[SkeletonWay]) {
  def withWay(way: SkeletonWay): Ring = Ring(ways :+ way)

  def startNodeId: Long = ways.head.nodeIds.head

  def size: Int = ways.size

}
