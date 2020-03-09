// this class is generated, please do not modify

import {List} from "immutable";
import {Ref} from "../common/ref";

export class RefDiffs {

  constructor(readonly removed: List<Ref>,
              readonly added: List<Ref>,
              readonly updated: List<Ref>) {
  }

  public static fromJSON(jsonObject: any): RefDiffs {
    if (!jsonObject) {
      return undefined;
    }
    return new RefDiffs(
      jsonObject.removed ? List(jsonObject.removed.map((json: any) => Ref.fromJSON(json))) : List(),
      jsonObject.added ? List(jsonObject.added.map((json: any) => Ref.fromJSON(json))) : List(),
      jsonObject.updated ? List(jsonObject.updated.map((json: any) => Ref.fromJSON(json))) : List()
    );
  }
}
