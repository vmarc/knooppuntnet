// this class is generated, please do not modify

import {Ref} from '../../common/ref';

export class RefChanges {

  constructor(readonly oldRefs: Array<Ref>,
              readonly newRefs: Array<Ref>) {
  }

  public static fromJSON(jsonObject: any): RefChanges {
    if (!jsonObject) {
      return undefined;
    }
    return new RefChanges(
      jsonObject.oldRefs.map((json: any) => Ref.fromJSON(json)),
      jsonObject.newRefs.map((json: any) => Ref.fromJSON(json))
    );
  }
}
