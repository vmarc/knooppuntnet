// this class is generated, please do not modify

import {Ref} from "../../common/ref";

export class RefBooleanChange {

  constructor(readonly ref: Ref,
              readonly after: boolean) {
  }

  public static fromJSON(jsonObject: any): RefBooleanChange {
    if (!jsonObject) {
      return undefined;
    }
    return new RefBooleanChange(
      Ref.fromJSON(jsonObject.ref),
      jsonObject.after
    );
  }
}
