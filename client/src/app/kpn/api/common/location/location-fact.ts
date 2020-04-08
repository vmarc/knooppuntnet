// this class is generated, please do not modify

import {List} from "immutable";
import {Fact} from "../../custom/fact";
import {Ref} from "../common/ref";

export class LocationFact {

  constructor(readonly elementType: string,
              readonly fact: Fact,
              readonly refs: List<Ref>) {
  }

  public static fromJSON(jsonObject: any): LocationFact {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationFact(
      jsonObject.elementType,
      Fact.fromJSON(jsonObject.fact),
      jsonObject.refs ? List(jsonObject.refs.map((json: any) => Ref.fromJSON(json))) : List()
    );
  }
}
