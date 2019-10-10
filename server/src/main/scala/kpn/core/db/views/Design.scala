package kpn.core.db.views

import kpn.core.util.Util.classNameOf

trait Design {
  def views: Seq[View]
  def name: String = classNameOf(this)
}
