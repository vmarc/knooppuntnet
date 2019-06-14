import {TranslationUnit} from "./translation-unit";
import {List} from "immutable";

export class TranslationFile {

  constructor(readonly sourceLanguage: string,
              readonly targetLanguage: string,
              readonly translationUnits: List<TranslationUnit>) {
  }

}
