// xliff = XML Localization Interchange File Format

import {TranslationFile} from "./translation-file";
import {TranslationUnit} from "./translation-unit";

export class XliffParser {

  constructor() {
  }

  public parse(xmlString: string): TranslationFile {
    const parser = new DOMParser();
    const xmlDocument = parser.parseFromString(xmlString, 'text/xml');
    return this.parseTranslationFile(xmlDocument);
  }

  private parseTranslationFile(xmlDocument: Document): TranslationFile {
    const translationFileElement = xmlDocument.getElementsByTagName('file').item(0);
    const sourceLanguage = translationFileElement.getAttribute('source-language');
    const targetLanguage = translationFileElement.getAttribute('target-language');
    const translationUnits = this.parseTranslationUnits(xmlDocument);
    return new TranslationFile(sourceLanguage, targetLanguage, translationUnits);
  }

  private parseTranslationUnits(xmlDocument: Document): TranslationUnit[] {
    const translationUnits: TranslationUnit[] = [];
    const translationUnitElements = xmlDocument.getElementsByTagName('trans-unit');
    for (let i = 0; i < translationUnitElements.length; i++) {
      const translationUnitElement = translationUnitElements.item(i);
      const id = translationUnitElement.getAttribute('id');
      const source = translationUnitElement.getElementsByTagName('source').item(0).childNodes[0].nodeValue;
      const targetElement = translationUnitElement.getElementsByTagName('target').item(0);
      const target = targetElement.childNodes[0].nodeValue;
      const state = targetElement.getAttribute('state');
      let sourceFile = "";
      let lineNumber = "";
      const contextGroup = translationUnitElement.getElementsByTagName('context-group').item(0);
      const contexts = contextGroup.getElementsByTagName('context');
      for(let j = 0; j < contexts.length; j++) {
        const context = contexts.item(j);
        const contextType = context.getAttribute("context-type");
        if ("sourcefile" == contextType) {
          sourceFile = context.childNodes[0].nodeValue;
        }
        else if ("linenumber" == contextType) {
          lineNumber = context.childNodes[0].nodeValue;
        }
      }

      const translationUnit = new TranslationUnit(id, source, target, state, sourceFile, lineNumber);
      translationUnits.push(translationUnit);
    }
    return translationUnits;
  }

}
