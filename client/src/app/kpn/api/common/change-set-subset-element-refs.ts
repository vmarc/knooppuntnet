// this class is generated, please do not modify

import {ChangeSetElementRefs} from "./change-set-element-refs";
import {Subset} from "../custom/subset";

export class ChangeSetSubsetElementRefs {

  constructor(readonly subset: Subset,
              readonly elementRefs: ChangeSetElementRefs) {
  }

  public static fromJSON(jsonObject: any): ChangeSetSubsetElementRefs {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetSubsetElementRefs(
      Subset.fromJSON(jsonObject.subset),
      ChangeSetElementRefs.fromJSON(jsonObject.elementRefs)
    );
  }
}
