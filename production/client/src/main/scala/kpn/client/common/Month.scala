package kpn.client.common

import kpn.client.common.Nls.nls

object Month {

  def name(number: String)(implicit context: Context): String = {
    number match {
      case "01" => nls("January", "Januari")
      case "02" => nls("February", "Februari")
      case "03" => nls("March", "Maart")
      case "04" => "April"
      case "05" => nls("May", "Mei")
      case "06" => nls("June", "Juni")
      case "07" => nls("July", "Juli")
      case "08" => nls("August", "Augustus")
      case "09" => "September"
      case "10" => nls("October", "Oktober")
      case "11" => "November"
      case "12" => "December"
      case _ => "?"
    }
  }
}
