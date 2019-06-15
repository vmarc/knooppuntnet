import {TranslationFile} from "./translation-file";
import {TranslationUnit} from "./translation-unit";
import {TranslationLocation} from "./translation-location";

export class XliffWriter {

  write(translationFile: TranslationFile): string {
    const fragments: Array<Array<string>> = [];
    fragments.push(this.translationFileStart(translationFile));
    translationFile.translationUnits.forEach(translationUnit => {
      fragments.push(this.translationUnitStart(translationUnit));
      translationUnit.locations.forEach(location => {
        fragments.push(this.translationLocation(location));
      });
      fragments.push(this.translationUnitEnd());
    });
    fragments.push(this.translationFileEnd());
    return fragments.map(a => a.join("\n")).join("\n");
  }

  private translationFileStart(translationFile: TranslationFile): Array<string> {
    return [
      `<?xml version=\"1.0\" encoding=\"UTF-8\"?>`,
      `<xliff version=\"1.2\" xmlns=\"urn:oasis:names:tc:xliff:document:1.2\">`,
      `  <file source-language="${translationFile.sourceLanguage}" datatype="plaintext" ` +
      `original="ng2.template" target-language="${translationFile.targetLanguage}">`,
      `    <body>`
    ];
  }

  private translationFileEnd(): Array<string> {
    return [
      "    </body>",
      "  </file>",
      "</xliff>\n"
    ];
  }

  private translationUnitStart(translationUnit: TranslationUnit): Array<string> {
    const state = translationUnit.state == null ? "" : " state=\"new\"";
    return [
      `      <trans-unit id="${translationUnit.id}" datatype="html">`,
      `        <source>${translationUnit.source}</source>`,
      `        <target ${state}>${translationUnit.target}</target>`
    ];
  }

  private translationUnitEnd(): Array<string> {
    return [
      "      </trans-unit>"
    ];
  }

  private translationLocation(location: TranslationLocation): Array<string> {
    return [
      `        <context-group purpose="location">`,
      `          <context context-type="sourcefile">${location.sourceFile}</context>`,
      `          <context context-type="linenumber">${location.lineNumber}</context>`,
      `        </context-group>`
    ];
  }

}
