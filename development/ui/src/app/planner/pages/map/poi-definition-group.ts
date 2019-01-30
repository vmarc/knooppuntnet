import {List} from "immutable";
import {PoiDefinition} from "./poi-definition";

export class PoiDefinitionGroup {

  constructor(readonly name: string,
              readonly definitions: List<PoiDefinition>) {
  }

}
