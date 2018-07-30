// this class is generated, please do not modify

import {Ref} from '../common/ref';

export class RefDiffs {

  constructor(public removed?: Array<Ref>,
              public added?: Array<Ref>,
              public updated?: Array<Ref>) {
  }

  public static fromJSON(jsonObject): RefDiffs {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RefDiffs();
    instance.removed = jsonObject.removed ? jsonObject.removed.map(json => Ref.fromJSON(json)) : [];
    instance.added = jsonObject.added ? jsonObject.added.map(json => Ref.fromJSON(json)) : [];
    instance.updated = jsonObject.updated ? jsonObject.updated.map(json => Ref.fromJSON(json)) : [];
    return instance;
  }
}

