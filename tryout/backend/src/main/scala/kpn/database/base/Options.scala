package kpn.database.base

trait Options[T] {

  def parse(args: Array[String]): Option[T]
}
