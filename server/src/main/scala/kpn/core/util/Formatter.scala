package kpn.core.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object Formatter {

  def percentage(count: Long, denominator: Long): String = {
    if (count == 0 || denominator == 0) {
      "-"
    }
    else {
      "%.02f%%".format(100d * count.toDouble / denominator).replaceAll("\\.", ",")
    }
  }

  def number(number: Long): String = {
    if (number == 0) {
      "-"
    }
    else {
      new DecimalFormat("#,##0", new DecimalFormatSymbols(new Locale("nl", "BE"))).format(number.toLong)
    }
  }
}
