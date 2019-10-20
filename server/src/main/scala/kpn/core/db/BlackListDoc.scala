package kpn.core.db

import kpn.server.analyzer.engine.changes.data.BlackList

case class BlackListDoc(_id: String, blackList: BlackList, _rev: Option[String] = None) extends Doc
