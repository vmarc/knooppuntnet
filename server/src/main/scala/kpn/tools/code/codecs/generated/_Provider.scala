package kpn.tools.code.codecs.generated

import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp
import kpn.tools.code.codecs.RawNodeCodec
import kpn.tools.code.codecs.ScalaLongCodec
import kpn.tools.code.codecs.TagCodec
import kpn.tools.code.codecs.TimestampCodec
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry

class _Provider extends CodecProvider {
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
