package kpn.core.util

class NaturalSortingTest extends UnitTest {

  test("0 Sorting node names") {
    performTest(
      Seq(
        "5",
        "10",
        "20",
        "03",
        "1"
      ),
      Seq(
        "1",
        "03",
        "5",
        "10",
        "20"
      )
    )
  }

  test("0 Sorting node names containing letters") {
    performTest(
      Seq(
        "A5",
        "A10",
        "A20",
        "A03",
        "A1"
      ),
      Seq(
        "A1",
        "A03",
        "A5",
        "A10",
        "A20"
      )
    )
  }

  // tests adapted from https://rosettacode.org/wiki/Natural_sorting#Scala

  test("1 Ignoring leading spaces") {
    performTest(
      Seq(
        "ignore leading spaces: 2-2",
        " ignore leading spaces: 2-1",
        "  ignore leading spaces: 2+0",
        "   ignore leading spaces: 2+1"
      ),
      Seq(
        "  ignore leading spaces: 2+0",
        "   ignore leading spaces: 2+1",
        " ignore leading spaces: 2-1",
        "ignore leading spaces: 2-2"
      )
    )
  }

  test("1 Ignoring multiple adjacent spaces (m.a.s)") {
    performTest(
      Seq(
        "ignore m.a.s spaces: 2-2",
        "ignore m.a.s  spaces: 2-1",
        "ignore m.a.s   spaces: 2+0",
        "ignore m.a.s  " +
          "spaces: 2+1"
      ),
      Seq(
        "ignore m.a.s   spaces: 2+0",
        "ignore m.a.s  spaces: 2+1",
        "ignore m.a.s  spaces: 2-1",
        "ignore m.a.s spaces: 2-2"
      )
    )
  }

  test("2 Equivalent whitespace characters") {
    performTest(
      Seq(
        "Equiv. spaces: 3-3",
        "Equiv.\rspaces: 3-2",
        "Equiv.\u000cspaces: 3-1",
        "Equiv.\u000bspaces: 3+0",
        "Equiv.\nspaces: 3+1",
        "Equiv.\tspaces: 3+2"
      ),
      Seq(
        "Equiv.\u000bspaces: 3+0",
        "Equiv.\nspaces: 3+1",
        "Equiv.\tspaces: 3+2",
        "Equiv.\u000cspaces: 3-1",
        "Equiv.\rspaces: 3-2",
        "Equiv. spaces: 3-3"
      )
    )
  }

  test("3 Case Independent sort") {
    performTest(
      Seq(
        "cASE INDEPENENT: 3-2",
        "caSE INDEPENENT: 3-1",
        "casE INDEPENENT: 3+0",
        "case INDEPENENT: 3+1"
      ),
      Seq(
        "casE INDEPENENT: 3+0",
        "case INDEPENENT: 3+1",
        "caSE INDEPENENT: 3-1",
        "cASE INDEPENENT: 3-2"
      )
    )
  }

  test("4 Numeric fields as numerics") {
    performTest(
      Seq(
        "foo100bar99baz0.txt",
        "foo100bar10baz0.txt",
        "foo1000bar99baz10.txt",
        "foo1000bar99baz9.txt"
      ),
      Seq(
        "foo100bar10baz0.txt",
        "foo100bar99baz0.txt",
        "foo1000bar99baz9.txt",
        "foo1000bar99baz10.txt"
      )
    )
  }

  test("5 Title sorts") {
    performTest(
      Seq(
        "The Wind in the Willows",
        "The 40th step more",
        "The 39 steps", "Wanda"
      ),
      Seq(
        "The 39 steps",
        "The 40th step more",
        "Wanda",
        "The Wind in the Willows"
      )
    )
  }

  test("6 Equivalent accented characters (and case)") {
    performTest(
      Seq(
        "Equiv. \u00fd accents: 2-2",
        "Equiv. \u00dd accents: 2-1",
        "Equiv. y accents: 2+0",
        "Equiv. Y accents: 2+1"
      ),
      Seq(
        "Equiv. y accents: 2+0",
        "Equiv. Y accents: 2+1",
        "Equiv. \u00dd accents: 2-1",
        "Equiv. \u00fd accents: 2-2"
      )
    )
  }

  test("7 Separated ligatures") {
    performTest(
      Seq(
        "\u0132 ligatured ij",
        "no ligature"
      ),
      Seq(
        "\u0132 ligatured ij",
        "no ligature"
      )
    )
  }

  test("8 Character replacements") {
    performTest(
      Seq(
        "Start with an \u0292: 2-2",
        "Start with an \u017f: 2-1",
        "Start with an \u00df: 2+0",
        "Start with an s: 2+1"
      ),
      Seq(
        "Start with an s: 2+1",
        "Start with an \u017f: 2-1",
        "Start with an \u0292: 2-2",
        "Start with an \u00df: 2+0"
      )
    )
  }

  test("sort EuroVelo route names") {
    val input = Seq(
      "EV1",
      "EV1-gpx",
      "EV10",
      "EV10-gpx",
      "EV11",
      "EV11-gpx",
      "EV11-gpx-full",
      "EV12",
      "EV12-gpx",
      "EV12-gpx-full",
      "EV13",
      "EV13-gpx",
      "EV14",
      "EV14-gpx",
      "EV14-gpx-full",
      "EV15",
      "EV15-gpx",
      "EV17",
      "EV17-gpx",
      "EV19",
      "EV19-gpx",
      "EV2",
      "EV2-gpx",
      "EV2-gpx-full",
      "EV3",
      "EV3-gpx",
      "EV4",
      "EV4-gpx",
      "EV4-multi-gpx",
      "EV5",
      "EV5-gpx",
      "EV6",
      "EV6-gpx",
      "EV7",
      "EV7-gpx",
      "EV8",
      "EV8-gpx",
      "EV9",
      "EV9-gpx",
      "EV9-gpx-full"
    )

    NaturalSorting.sortBy(input)(s => s + "-").shouldMatchTo(
      Seq(
        "EV1",
        "EV1-gpx",
        "EV2",
        "EV2-gpx",
        "EV2-gpx-full",
        "EV3",
        "EV3-gpx",
        "EV4",
        "EV4-gpx",
        "EV4-multi-gpx",
        "EV5",
        "EV5-gpx",
        "EV6",
        "EV6-gpx",
        "EV7",
        "EV7-gpx",
        "EV8",
        "EV8-gpx",
        "EV9",
        "EV9-gpx",
        "EV9-gpx-full",
        "EV10",
        "EV10-gpx",
        "EV11",
        "EV11-gpx",
        "EV11-gpx-full",
        "EV12",
        "EV12-gpx",
        "EV12-gpx-full",
        "EV13",
        "EV13-gpx",
        "EV14",
        "EV14-gpx",
        "EV14-gpx-full",
        "EV15",
        "EV15-gpx",
        "EV17",
        "EV17-gpx",
        "EV19",
        "EV19-gpx",
      )
    )
  }

  private def performTest(input: Seq[String], expected: Seq[String]): Unit = {
    NaturalSorting.sort(input).shouldMatchTo(expected)
  }
}
