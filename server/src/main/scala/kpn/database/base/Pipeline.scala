package kpn.database.base

import org.mongodb.scala.bson.conversions.Bson

case class Pipeline(name: String, stages: Seq[Bson])
