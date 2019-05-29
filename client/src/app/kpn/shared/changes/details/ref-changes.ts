// this class is generated, please do not modify

import {List} from "immutable";
import {Ref} from "../../common/ref";

export class RefChanges {

  constructor(readonly oldRefs: List<Ref>,
              readonly newRefs: List<Ref>) {
  }

  public static fromJSON(jsonObject): RefChanges {
    if (!jsonObject) {
      return undefined;
    }
    return new RefChanges(
      jsonObject.oldRefs ? List(jsonObject.oldRefs.map(json => Ref.fromJSON(json))) : List(),
      jsonObject.newRefs ? List(jsonObject.newRefs.map(json => Ref.fromJSON(json))) : List()
    );
  }
}
