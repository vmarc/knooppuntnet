package kpn.database.base

import kpn.database.base.Types.MongoPipeline

case class Pipeline(name: String, stages: MongoPipeline)
