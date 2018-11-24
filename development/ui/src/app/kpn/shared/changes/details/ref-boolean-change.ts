// this class is generated, please do not modify

import {Ref} from '../../common/ref';

export class RefBooleanChange {
  readonly ref: Ref;
  readonly after: boolean;

  constructor(ref: Ref,
              after: boolean) {
    this.ref = ref;
    this.after = after;
  }

  public static fromJSON(jsonObject): RefBooleanChange {
    if (!jsonObject) {
      return undefined;
    }
    return new RefBooleanChange(
      Ref.fromJSON(jsonObject.ref),
      jsonObject.after
    );
  }
}
