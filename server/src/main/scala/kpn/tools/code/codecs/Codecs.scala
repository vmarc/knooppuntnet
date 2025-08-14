package kpn.tools.code.codecs

import kpn.core.util.Log
import kpn.tools.code.ClassId

object Codecs {

  val log: Log = Log(classOf[Codecs])
  val CodecDir = "src/main/scala/kpn/tools/code/codecs/generated"

  val customCodecs: Seq[ClassId] = Seq(
    ClassId("Day", "kpn.api.custom"),
    ClassId("Tag", "kpn.api.custom"),
    ClassId("Timestamp", "kpn.api.custom"),
    ClassId("ApiResponse", "kpn.api.custom"),
  )
}

class Codecs
