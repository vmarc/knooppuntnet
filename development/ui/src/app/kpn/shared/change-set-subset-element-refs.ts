// this class is generated, please do not modify

import {ChangeSetElementRefs} from './change-set-element-refs';
import {Subset} from './subset';

export class ChangeSetSubsetElementRefs {
  readonly subset: Subset;
  readonly elementRefs: ChangeSetElementRefs;

  constructor(subset: Subset,
              elementRefs: ChangeSetElementRefs) {
    this.subset = subset;
    this.elementRefs = elementRefs;
  }

  public static fromJSON(jsonObject): ChangeSetSubsetElementRefs {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetSubsetElementRefs(
      Subset.fromJSON(jsonObject.subset),
      ChangeSetElementRefs.fromJSON(jsonObject.elementRefs)
    );
  }
}
