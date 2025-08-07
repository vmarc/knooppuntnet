package kpn.tools.code.codecs

import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry

class KpnCodecProvider extends CodecProvider {
  override def get[T](aClass: Class[T], codecRegistry: CodecRegistry): Codec[T] = {
    if (aClass == classOf[Long]) {
      return new ScalaLongCodec(codecRegistry).asInstanceOf[Codec[T]]
    }
    if (aClass == classOf[Tag]) {
      return new TagCodec(codecRegistry).asInstanceOf[Codec[T]]
    }
    if (aClass == classOf[Timestamp]) {
      return new TimestampCodec(codecRegistry).asInstanceOf[Codec[T]]
    }
    if (aClass == classOf[RawNode]) {
      return new RawNodeCodec(codecRegistry).asInstanceOf[Codec[T]]
    }
    null
  }
}
