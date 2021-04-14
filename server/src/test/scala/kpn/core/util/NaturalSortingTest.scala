package kpn.core.util

class NaturalSortingTest extends UnitTest {

  test("0 Sorting node names") {
    performTest(
      List(
        "5",
        "10",
        "20",
        "03",
        "1"
      ),
      List(
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
      List(
        "A5",
        "A10",
        "A20",
        "A03",
        "A1"
      ),
      List(
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
      List(
        "ignore leading spaces: 2-2",
        " ignore leading spaces: 2-1",
        "  ignore leading spaces: 2+0",
        "   ignore leading spaces: 2+1"
      ),
      List(
        "  ignore leading spaces: 2+0",
        "   ignore leading spaces: 2+1",
        " ignore leading spaces: 2-1",
        "ignore leading spaces: 2-2"
      )
    )
  }

  test("1 Ignoring multiple adjacent spaces (m.a.s)") {
    performTest(
      List(
        "ignore m.a.s spaces: 2-2",
        "ignore m.a.s  spaces: 2-1",
        "ignore m.a.s   spaces: 2+0",
        "ignore m.a.s  " +
          "spaces: 2+1"
      ),
      List(
        "ignore m.a.s   spaces: 2+0",
        "ignore m.a.s  spaces: 2+1",
        "ignore m.a.s  spaces: 2-1",
        "ignore m.a.s spaces: 2-2"
      )
    )
  }

  test("2 Equivalent whitespace characters") {
    performTest(
      List(
        "Equiv. spaces: 3-3",
        "Equiv.\rspaces: 3-2",
        "Equiv.\u000cspaces: 3-1",
        "Equiv.\u000bspaces: 3+0",
        "Equiv.\nspaces: 3+1",
        "Equiv.\tspaces: 3+2"
      ),
      List(
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
      List(
        "cASE INDEPENENT: 3-2",
        "caSE INDEPENENT: 3-1",
        "casE INDEPENENT: 3+0",
        "case INDEPENENT: 3+1"
      ),
      List(
        "casE INDEPENENT: 3+0",
        "case INDEPENENT: 3+1",
        "caSE INDEPENENT: 3-1",
        "cASE INDEPENENT: 3-2"
      )
    )
  }

  test("4 Numeric fields as numerics") {
    performTest(
      List(
        "foo100bar99baz0.txt",
        "foo100bar10baz0.txt",
        "foo1000bar99baz10.txt",
        "foo1000bar99baz9.txt"
      ),
      List(
        "foo100bar10baz0.txt",
        "foo100bar99baz0.txt",
        "foo1000bar99baz9.txt",
        "foo1000bar99baz10.txt"
      )
    )
  }

  test("5 Title sorts") {
    performTest(
      List(
        "The Wind in the Willows",
        "The 40th step more",
        "The 39 steps", "Wanda"
      ),
      List(
        "The 39 steps",
        "The 40th step more",
        "Wanda",
        "The Wind in the Willows"
      )
    )
  }

  test("6 Equivalent accented characters (and case)") {
    performTest(
      List(
        "Equiv. \u00fd accents: 2-2",
        "Equiv. \u00dd accents: 2-1",
        "Equiv. y accents: 2+0",
        "Equiv. Y accents: 2+1"
      ),
      List(
        "Equiv. y accents: 2+0",
        "Equiv. Y accents: 2+1",
        "Equiv. \u00dd accents: 2-1",
        "Equiv. \u00fd accents: 2-2"
      )
    )
  }

  test("7 Separated ligatures") {
    performTest(
      List(
        "\u0132 ligatured ij",
        "no ligature"
      ),
      List(
        "\u0132 ligatured ij",
        "no ligature"
      )
    )
  }

  test("8 Character replacements") {
    performTest(
      List(
        "Start with an \u0292: 2-2",
        "Start with an \u017f: 2-1",
        "Start with an \u00df: 2+0",
        "Start with an s: 2+1"
      ),
      List(
        "Start with an s: 2+1",
        "Start with an \u017f: 2-1",
        "Start with an \u0292: 2-2",
        "Start with an \u00df: 2+0"
      )
    )
  }

  private def performTest(input: Seq[String], expected: Seq[String]): Unit = {
    NaturalSorting.sort(input) should matchTo(expected)
  }

}
