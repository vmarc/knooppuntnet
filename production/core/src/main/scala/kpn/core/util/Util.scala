package kpn.core.util

import scala.annotation.tailrec

object Util {

  def withoutSuccessiveDuplicates[A](objects: Seq[A]): Seq[A] = {
    recursiveWithoutSuccessiveDuplicates(List(), objects).reverse
  }

  @tailrec
  private def recursiveWithoutSuccessiveDuplicates[A](objects: List[A], remainingObjects: Seq[A]): Seq[A] = {
    if (remainingObjects.isEmpty) {
      objects
    }
    else {
      val obj = remainingObjects.head
      if (objects.isEmpty || objects.head != obj) {
        recursiveWithoutSuccessiveDuplicates(obj :: objects, remainingObjects.tail)
      }
      else {
        recursiveWithoutSuccessiveDuplicates(objects, remainingObjects.tail)
      }
    }
  }

  def classNameOf(obj: Any): String = obj.getClass.getSimpleName.filterNot(_ == '$')

  def split[T](separator: T, list: Seq[T]): Seq[Seq[T]] = {
    split(separator, list, Seq())
  }

  @tailrec
  private def split[T](separator: T, list: Seq[T], tmp: Seq[Seq[T]]): Seq[Seq[T]] = {
    list.indexOf(separator) match {
      case -1 => tmp ++ Seq(list)
      case i =>
        val s = list.splitAt(i)
        split(separator, s._2.tail, tmp ++ Seq(s._1))
    }
  }
}
