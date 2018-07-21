import {TranslationUnit} from "./translation-unit";

export class TranslationFile {

  constructor(public sourceLanguage: string,
              public targetLanguage: string,
              public translationUnits: TranslationUnit[]) {
  }

}
