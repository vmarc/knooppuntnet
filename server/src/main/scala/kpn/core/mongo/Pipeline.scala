package kpn.core.mongo

import org.mongodb.scala.bson.conversions.Bson

case class Pipeline(name: String, stages: Seq[Bson])
