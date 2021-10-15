package kpn.core.taginfo

case class TagInfoProject(
  name: String = "knooppuntnet",
  description: String = "Route planner and quality analysis for walking and cycling networks",
  project_url: String = "https://github.com/vmarc/knooppuntnet",
  doc_url: Option[String] = None, // documentation of the project and especially the tags used
  icon_url: String = "https://raw.githubusercontent.com/vmarc/knooppuntnet/develop/docs/logo.svg",
  contact_name: String = "Marc Vergouwen",
  contact_email: String = "taginfo@vmarc.be"
)
