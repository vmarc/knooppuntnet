package kpn.core.util

class Report {
  private var indentLevel = 0

  def indent(block: => Unit): Unit = {
    indentLevel = indentLevel + 1
    block
    indentLevel = indentLevel - 1
  }

  def print(string: String): Unit = {
    0.until (indentLevel).foreach { _ =>
      System.out.print("  ")
    }
    println(string)
  }
}
