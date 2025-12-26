package kpn.server.analyzer.engine.tiles.domain

import kpn.core.util.CoordinateUtil
import kpn.core.util.UnitTest

class CoordinateCodecTest extends UnitTest {

  test("encode / decode large coordinate list") {
    val coordinateString = "[[-96.7162281,46.4441999],[-96.7156515,46.4441919],[-96.71498,46.4441818],[-96.7145696,46.4441393],[-96.7142263,46.4440709],[-96.7138642,46.443973],[-96.7135397,46.4438436],[-96.7132473,46.4436976],[-96.7121878,46.442999],[-96.7111686,46.442402],[-96.7102084,46.4419233],[-96.7098114,46.4417421],[-96.7093071,46.4415739],[-96.7087573,46.4414871],[-96.7080626,46.4414427],[-96.7076495,46.4414196],[-96.7075492,46.4414177],[-96.7070045,46.4413752],[-96.7066303,46.4412967],[-96.7062682,46.4411858],[-96.705949,46.4410435],[-96.7056218,46.4408531],[-96.7048252,46.4403614],[-96.7044121,46.440123],[-96.7038793,46.4399011],[-96.7033421,46.4397703],[-96.7028081,46.4397145],[-96.6989592,46.4395556],[-96.6984415,46.4395241],[-96.6979399,46.4394742],[-96.6973954,46.4394077],[-96.6951317,46.4390861],[-96.6922068,46.4386692],[-96.6918236,46.4386141],[-96.6914967,46.4385729],[-96.6912137,46.4385426],[-96.690833,46.4385111],[-96.6903945,46.438485],[-96.6881056,46.438417],[-96.6874807,46.4383966],[-96.6871683,46.438385],[-96.6869354,46.438377],[-96.6865898,46.438368],[-96.6857306,46.4383436],[-96.6851763,46.4373967],[-96.684585,46.4364269],[-96.6840347,46.4354993],[-96.6839458,46.4353499],[-96.683876,46.4352592],[-96.6838122,46.4352057],[-96.6837275,46.4351419],[-96.6836221,46.435082],[-96.6834933,46.4350381],[-96.6833877,46.435012],[-96.683284,46.4349955],[-96.6831664,46.4349859],[-96.6827903,46.4349805],[-96.6823302,46.4349812],[-96.6768757,46.4349731],[-96.676926,46.4375677],[-96.6769331,46.4377826],[-96.6769579,46.4381977],[-96.6770312,46.4387231],[-96.6771265,46.4390316],[-96.6772575,46.4393716],[-96.6773517,46.4396145],[-96.6775743,46.4400246],[-96.6777114,46.4402391],[-96.6779135,46.4404864],[-96.6781261,46.4407342],[-96.6784073,46.4410311],[-96.6787372,46.4413484],[-96.6791419,46.4417408],[-96.6793201,46.4419153]]"
    val encoded = encode(coordinateString)
    val decodedCoordinateString = decode(encoded)
    decodedCoordinateString should equal(coordinateString)
    encoded.take(60) should equal("[[-96.7162281,46.4441999],[5766,-80],[6715,-101],[4104,-425]")
  }

  test("encode / decode small coordinate list") {
    val coordinateString = "[[5.1234,51.4321],[5.2345,51.5432],[5.3456,51.6543]]"
    val encoded = encode(coordinateString)
    val decodedCoordinateString = decode(encoded)
    decodedCoordinateString should equal(coordinateString)
    encoded should equal("[[5.1234,51.4321],[1111000,1111000],[1111000,1111000]]")
  }

  test("encode / decode empty coordinate string") {
    val coordinateString = ""
    val encoded = encode(coordinateString)
    val decodedCoordinateString = decode(encoded)
    decodedCoordinateString should equal("[]")
    encoded should equal("[]")
  }

  test("encode / decode empty coordinate list") {
    val coordinateString = "[]"
    val encoded = encode(coordinateString)
    val decodedCoordinateString = decode(encoded)
    decodedCoordinateString should equal(coordinateString)
    encoded should equal("[]")
  }

  test("encode / decode single coordinate") {
    val coordinateString = "[[5.1234,51.4321]]"
    val encoded = encode(coordinateString)
    val decodedCoordinateString = decode(encoded)
    decodedCoordinateString should equal(coordinateString)
    encoded should equal("[[5.1234,51.4321]]")
  }

  test("encode / decode negative coordinates") {
    val coordinateString = "[[-5.1234,-51.4321],[-5.2345,-51.5432]]"
    val encoded = encode(coordinateString)
    val decodedCoordinateString = decode(encoded)
    decodedCoordinateString should equal(coordinateString)
    encoded should equal("[[-5.1234,-51.4321],[-1111000,-1111000]]")
  }

  private def encode(coordinateString: String): String = {
    if (coordinateString.isEmpty) {
      return "[]"
    }
    val coordinates = CoordinateUtil.stringToCoordinates(coordinateString)
    CoordinateCodec.encode(coordinates)
  }

  private def decode(encodedCoordinateString: String): String = {
    val decoded = CoordinateCodec.decode(encodedCoordinateString)
    CoordinateUtil.coordinatesToString(decoded)
  }

  private def encodeAndDecode(coordinateString: String): String = {
    val coordinates = CoordinateUtil.stringToCoordinates(coordinateString)
    val encoded = CoordinateCodec.encode(coordinates)
    val decoded = CoordinateCodec.decode(encoded)
    CoordinateUtil.coordinatesToString(decoded)
  }
}
