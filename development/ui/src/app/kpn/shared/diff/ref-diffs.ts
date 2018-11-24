// this class is generated, please do not modify

import {List} from 'immutable';
import {Ref} from '../common/ref';

export class RefDiffs {
  readonly removed: List<Ref>;
  readonly added: List<Ref>;
  readonly updated: List<Ref>;

  constructor(removed: List<Ref>,
              added: List<Ref>,
              updated: List<Ref>) {
    this.removed = removed;
    this.added = added;
    this.updated = updated;
  }

  public static fromJSON(jsonObject): RefDiffs {
    if (!jsonObject) {
      return undefined;
    }
    return new RefDiffs(
      jsonObject.removed ? List(jsonObject.removed.map(json => Ref.fromJSON(json))) : List(),
      jsonObject.added ? List(jsonObject.added.map(json => Ref.fromJSON(json))) : List(),
      jsonObject.updated ? List(jsonObject.updated.map(json => Ref.fromJSON(json))) : List()
    );
  }
}
