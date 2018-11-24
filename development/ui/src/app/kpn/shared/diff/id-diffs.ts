// this class is generated, please do not modify

import {List} from 'immutable';

export class IdDiffs {
  readonly removed: List<number>;
  readonly added: List<number>;
  readonly updated: List<number>;

  constructor(removed: List<number>,
              added: List<number>,
              updated: List<number>) {
    this.removed = removed;
    this.added = added;
    this.updated = updated;
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
