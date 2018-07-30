// this class is generated, please do not modify

import {Ref} from '../../common/ref';

export class RefChanges {

  constructor(public oldRefs?: Array<Ref>,
              public newRefs?: Array<Ref>) {
  }

  public static fromJSON(jsonObject): RefChanges {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RefChanges();
    instance.oldRefs = jsonObject.oldRefs ? jsonObject.oldRefs.map(json => Ref.fromJSON(json)) : [];
    instance.newRefs = jsonObject.newRefs ? jsonObject.newRefs.map(json => Ref.fromJSON(json)) : [];
    return instance;
  }
}

