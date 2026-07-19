package kpn.tools.code

import java.io.PrintStream

class IndentingPrintStream(out: PrintStream) {

  private var indentLevel = 0

  def indent(block: => Unit): Unit = {
    indentLevel = indentLevel + 1
    block
    indentLevel = indentLevel - 1
  }

  def println(string: String): Unit = {
    0.until(indentLevel).foreach { _ =>
      out.print("  ")
    }
    out.println(string)
  }

  def skipLine(): Unit = {
    out.println()
  }

  def close(): Unit = {
    out.close()
  }
}
