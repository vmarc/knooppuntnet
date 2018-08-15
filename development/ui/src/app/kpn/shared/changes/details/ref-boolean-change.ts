// this class is generated, please do not modify

import {Ref} from '../../common/ref';

export class RefBooleanChange {

  constructor(public ref?: Ref,
              public after?: boolean) {
  }

  public static fromJSON(jsonObject): RefBooleanChange {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RefBooleanChange();
    instance.ref = Ref.fromJSON(jsonObject.ref);
    instance.after = jsonObject.after;
    return instance;
  }
}

