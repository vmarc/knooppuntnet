package kpn.core.tools.country

case class SkeletonData(
  nodes: Map[Long, SkeletonNode],
  ways: Map[Long, SkeletonWay],
  relations: Map[Long, SkeletonRelation]
)
