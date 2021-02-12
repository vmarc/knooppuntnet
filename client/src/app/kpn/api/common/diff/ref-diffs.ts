// this class is generated, please do not modify

import {Ref} from '../common/ref';

export class RefDiffs {

  constructor(readonly removed: Array<Ref>,
              readonly added: Array<Ref>,
              readonly updated: Array<Ref>) {
  }

  public static fromJSON(jsonObject: any): RefDiffs {
    if (!jsonObject) {
      return undefined;
    }
    return new RefDiffs(
      jsonObject.removed.map((json: any) => Ref.fromJSON(json)),
      jsonObject.added.map((json: any) => Ref.fromJSON(json)),
      jsonObject.updated.map((json: any) => Ref.fromJSON(json))
    );
  }
}
