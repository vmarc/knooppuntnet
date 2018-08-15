// this class is generated, please do not modify

import {ChangeSetElementRefs} from './change-set-element-refs';
import {Subset} from './subset';

export class ChangeSetSubsetElementRefs {

  constructor(public subset?: Subset,
              public elementRefs?: ChangeSetElementRefs) {
  }

  public static fromJSON(jsonObject): ChangeSetSubsetElementRefs {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangeSetSubsetElementRefs();
    instance.subset = Subset.fromJSON(jsonObject.subset);
    instance.elementRefs = ChangeSetElementRefs.fromJSON(jsonObject.elementRefs);
    return instance;
  }
}

