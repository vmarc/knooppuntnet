package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.core.util.UnitTest

class NoteTagAnalyzerTest extends UnitTest {

  test("isDeprecatedNoteTag") {
    NoteTagAnalyzer.isDeprecatedNoteTag("1-2") should equal(true)
    NoteTagAnalyzer.isDeprecatedNoteTag("01-02") should equal(true)
    NoteTagAnalyzer.isDeprecatedNoteTag("01 - 02") should equal(true)
    NoteTagAnalyzer.isDeprecatedNoteTag("a-b - c-d") should equal(true)
    NoteTagAnalyzer.isDeprecatedNoteTag("bla") should equal(false)
    NoteTagAnalyzer.isDeprecatedNoteTag(" ") should equal(false)
    NoteTagAnalyzer.isDeprecatedNoteTag(" - ") should equal(false)
    NoteTagAnalyzer.isDeprecatedNoteTag("- bla") should equal(false)
    NoteTagAnalyzer.isDeprecatedNoteTag("bla -") should equal(false)
  }
}
