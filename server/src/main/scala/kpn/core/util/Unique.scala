package kpn.core.util

import scala.annotation.tailrec

object Unique {

  def filter[A](objects: Seq[A]): Seq[A] = {
    recursivelyFilter(Seq(), objects)
  }

  @tailrec
  private def recursivelyFilter[A](objects: Seq[A], remainingObjects: Seq[A]): Seq[A] = {
    if (remainingObjects.isEmpty) {
      objects
    }
    else {
      val obj = remainingObjects.head
      if (objects.contains(obj)) {
        recursivelyFilter(objects, remainingObjects.tail)
      }
      else {
        recursivelyFilter(objects :+ obj, remainingObjects.tail)
      }
    }
  }

  def filter[A](objects: Seq[A], f: (A, A) => Boolean): Seq[A] = {
    recursivelyFilter(Seq(), objects, f)
  }

  @tailrec
  private def recursivelyFilter[A](objects: Seq[A], remainingObjects: Seq[A], f: (A, A) => Boolean): Seq[A] = {
    if (remainingObjects.isEmpty) {
      objects
    }
    else {
      val obj = remainingObjects.head
      if (objects.exists(o =>  f(o, obj))) {
        recursivelyFilter(objects, remainingObjects.tail, f)
      }
      else {
        recursivelyFilter(objects :+ obj, remainingObjects.tail, f)
      }
    }
  }

}
