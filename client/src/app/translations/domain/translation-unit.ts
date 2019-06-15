import {TranslationLocation} from "./translation-location";
import {List} from "immutable";

export class TranslationUnit {

  constructor(readonly id: string,
              readonly source: string,
              readonly target: string,
              readonly state: string,
              readonly locations: List<TranslationLocation>,
              readonly dirty: boolean) {
  }

}
