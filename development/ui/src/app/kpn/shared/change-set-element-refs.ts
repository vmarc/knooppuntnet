// this class is generated, please do not modify

import {ChangeSetElementRef} from './change-set-element-ref';

export class ChangeSetElementRefs {

  constructor(public removed?: Array<ChangeSetElementRef>,
              public added?: Array<ChangeSetElementRef>,
              public updated?: Array<ChangeSetElementRef>) {
  }

  public static fromJSON(jsonObject): ChangeSetElementRefs {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangeSetElementRefs();
    instance.removed = jsonObject.removed ? jsonObject.removed.map(json => ChangeSetElementRef.fromJSON(json)) : [];
    instance.added = jsonObject.added ? jsonObject.added.map(json => ChangeSetElementRef.fromJSON(json)) : [];
    instance.updated = jsonObject.updated ? jsonObject.updated.map(json => ChangeSetElementRef.fromJSON(json)) : [];
    return instance;
  }
}

