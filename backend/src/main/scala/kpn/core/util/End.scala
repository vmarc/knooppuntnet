package kpn.core.util

object End {
  def map[T, P](list: Seq[T])(f: (T, Option[Boolean]) => P): Seq[P] = {
    val ends: Seq[Option[Boolean]] = if (list.isEmpty) {
      Seq.empty
    }
    else {
      (1 until list.size).map(i => None) ++ Seq(Some(true))
    }
    list.zip(ends).map { case (element, end) => f(element, end)}
  }
}
