import {TranslationFile} from "./translation-file";
import {TranslationUnit} from "./translation-unit";

export class XliffWriter {

  constructor() {
  }

  public write(translationFile: TranslationFile): string {
    const fragments: Array<Array<string>> = [];
    fragments.push(this.translationFileStart(translationFile));
    translationFile.translationUnits.forEach(translationUnit => {
      fragments.push(this.translationUnitFragment(translationUnit));
    });
    fragments.push(this.translationFileEnd(translationFile));
    return fragments.map(a => a.join("\n")).join("\n");
  }

  private translationFileStart(translationFile: TranslationFile): Array<string> {
    return [
      '<?xml version="1.0" encoding="UTF-8"?>',
      '<xliff version="1.2" xmlns="urn:oasis:names:tc:xliff:document:1.2">',
      '  <file source-language="' + translationFile.sourceLanguage + '" datatype="plaintext" original="ng2.template" target-language="' +
      translationFile.targetLanguage + '">',
      '    <body>'
    ];
  }

  private translationFileEnd(translationFile: TranslationFile): Array<string> {
    return [
      '    </body>',
      '  </file>',
      '</xliff>\n'
    ];
  }

  private translationUnitFragment(translationUnit: TranslationUnit): Array<string> {
    return [
      '      <trans-unit id="' + translationUnit.id + '" datatype="html">',
      '        <source>' + translationUnit.source + '</source>',
      '        <target' + (translationUnit.state == null ? '' : ' state="new"') + '>' + translationUnit.target + '</target>',
      '        <context-group purpose="location">',
      '          <context context-type="sourcefile">' + translationUnit.sourceFile + '</context>',
      '          <context context-type="linenumber">' + translationUnit.lineNumber + '</context>',
      '        </context-group>',
      '      </trans-unit>'
    ];
  }

}
