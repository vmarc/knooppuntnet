package kpn.backend

class UserParser {

  def parse(xml: scala.xml.Node): Option[String] = {
    val user = xml.child \\ "user"
    val displayNam = (user \ "@display_name").text
    if (displayNam.isEmpty) None else Some(displayNam)
  }

}
