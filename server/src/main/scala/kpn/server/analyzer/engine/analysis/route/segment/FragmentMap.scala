package kpn.server.analyzer.engine.analysis.route.segment

object FragmentMap {
  def apply(fragments: Seq[Fragment]): FragmentMap = {
    val fragmentsWithIds = fragments.toVector.zipWithIndex.map { case (fragment, index) => fragment.copy(id = index) }
    new FragmentMap(fragmentsWithIds)
  }
}

class FragmentMap(fragments: Vector[Fragment]) {

  def apply(index: Int): Fragment = {
    fragments(index)
  }

  def size: Int = fragments.size

  def isEmpty: Boolean = fragments.isEmpty

  def ids: Vector[Int] = fragments.map(_.id)

  def all: Vector[Fragment] = fragments
}
