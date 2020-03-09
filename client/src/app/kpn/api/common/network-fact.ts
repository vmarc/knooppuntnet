// this class is generated, please do not modify

import {List} from "immutable";
import {Check} from "./check";
import {Ref} from "./common/ref";

export class NetworkFact {

  constructor(readonly name: string,
              readonly elementType: string,
              readonly elementIds: List<number>,
              readonly elements: List<Ref>,
              readonly checks: List<Check>) {
  }

  public static fromJSON(jsonObject: any): NetworkFact {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkFact(
      jsonObject.name,
      jsonObject.elementType,
      jsonObject.elementIds ? List(jsonObject.elementIds) : List(),
      jsonObject.elements ? List(jsonObject.elements.map((json: any) => Ref.fromJSON(json))) : List(),
      jsonObject.checks ? List(jsonObject.checks.map((json: any) => Check.fromJSON(json))) : List()
    );
  }
}
