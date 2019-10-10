package kpn.core.util

object Triplet {
  def slide[A](list: Seq[A]): Seq[Triplet[A]] = {
    list.indices.map { index =>
      val current = list(index)
      val previous = if (index == 0) None else Some(list(index - 1))
      val next = if (index < (list.size - 1)) Some(list(index + 1)) else None
      new Triplet[A](previous, current, next)
    }
  }
}

case class Triplet[A](previous: Option[A], current: A, next: Option[A])
