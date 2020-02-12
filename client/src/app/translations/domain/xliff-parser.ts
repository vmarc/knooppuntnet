// xliff = XML Localization Interchange File Format

import {TranslationFile} from "./translation-file";
import {TranslationUnit} from "./translation-unit";
import {List} from "immutable";
import {TranslationLocation} from "./translation-location";

export class XliffParser {

  parse(xmlString: string): TranslationFile {
    const parser = new DOMParser();
    const xmlDocument = parser.parseFromString(xmlString, "text/xml");
    return this.parseTranslationFile(xmlDocument);
  }

  private parseTranslationFile(xmlDocument: Document): TranslationFile {
    const translationFileElement = xmlDocument.getElementsByTagName("file").item(0);
    const sourceLanguage = translationFileElement.getAttribute("source-language");
    const targetLanguage = translationFileElement.getAttribute("target-language");
    const translationUnits = this.parseTranslationUnits(xmlDocument);
    return new TranslationFile(sourceLanguage, targetLanguage, translationUnits);
  }

  private parseTranslationUnits(xmlDocument: Document): List<TranslationUnit> {
    const translationUnits: Array<TranslationUnit> = [];
    const translationUnitElements = xmlDocument.getElementsByTagName("trans-unit");
    for (let i = 0; i < translationUnitElements.length; i++) {
      const translationUnitElement = translationUnitElements.item(i);
      const id = translationUnitElement.getAttribute("id");
      const source = translationUnitElement.getElementsByTagName("source").item(0).innerHTML
        // tslint:disable-next-line:quotemark
        .split('xmlns="urn:oasis:names:tc:xliff:document:1.2"').join("");
      const targetElement = translationUnitElement.getElementsByTagName("target").item(0);
      // tslint:disable-next-line:quotemark
      const target = targetElement.innerHTML.split('xmlns="urn:oasis:names:tc:xliff:document:1.2"').join("");
      const state = targetElement.getAttribute("state");

      let locations: List<TranslationLocation> = List();
      const contextGroups = translationUnitElement.getElementsByTagName("context-group");
      for (let k = 0; k < contextGroups.length; k++) {
        const contextGroup = contextGroups.item(k);
        const contexts = contextGroup.getElementsByTagName("context");
        let sourceFile = "";
        let lineNumber = 0;
        for (let j = 0; j < contexts.length; j++) {
          const context = contexts.item(j);
          const contextType = context.getAttribute("context-type");
          if ("sourcefile" === contextType) {
            sourceFile = context.childNodes[0].nodeValue;
          } else if ("linenumber" === contextType) {
            lineNumber = +context.childNodes[0].nodeValue;
          }
        }
        locations = locations.push(new TranslationLocation(sourceFile, lineNumber));
      }
      const translationUnit = new TranslationUnit(id, source, target, state, locations, false);
      translationUnits.push(translationUnit);
    }
    return List(translationUnits);
  }

}
