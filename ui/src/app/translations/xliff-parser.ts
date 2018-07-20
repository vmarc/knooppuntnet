// xliff = XML Localization Interchange File Format

import {TranslationUnit} from "./translation-unit";

export class XliffParser {

  constructor() {
  }

  public parse(xmlString: string): TranslationUnit[] {
    const translationUnits: TranslationUnit[] = [];
    const parser = new DOMParser();
    const xmlDocument = parser.parseFromString(xmlString, 'text/xml');
    const translationUnitElements = xmlDocument.getElementsByTagName('trans-unit');
    for (let i = 0; i < translationUnitElements.length; i++) {
      const translationUnitElement = translationUnitElements.item(i);
      const id = translationUnitElement.getAttribute('id');
      const source = translationUnitElement.getElementsByTagName('source')[0].childNodes[0].nodeValue;
      const target = translationUnitElement.getElementsByTagName('target')[0].childNodes[0].nodeValue;
      const translationUnit = new TranslationUnit(id, source, target);
      translationUnits.push(translationUnit);
    }
    return translationUnits;
  }

}
