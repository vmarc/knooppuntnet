// this class is generated, please do not modify

import {List} from 'immutable';

export class IdDiffs {

  constructor(readonly removed: List<number>,
              readonly added: List<number>,
              readonly updated: List<number>) {
  }

  public static fromJSON(jsonObject): IdDiffs {
    if (!jsonObject) {
      return undefined;
    }
    return new IdDiffs(
      jsonObject.removed ? List(jsonObject.removed) : List(),
      jsonObject.added ? List(jsonObject.added) : List(),
      jsonObject.updated ? List(jsonObject.updated) : List()
    );
  }
}
