// this class is generated, please do not modify

import {List} from 'immutable';
import {ChangeSetElementRef} from './change-set-element-ref';

export class ChangeSetElementRefs {
  readonly removed: List<ChangeSetElementRef>;
  readonly added: List<ChangeSetElementRef>;
  readonly updated: List<ChangeSetElementRef>;

  constructor(removed: List<ChangeSetElementRef>,
              added: List<ChangeSetElementRef>,
              updated: List<ChangeSetElementRef>) {
    this.removed = removed;
    this.added = added;
    this.updated = updated;
  }

  public static fromJSON(jsonObject): ChangeSetElementRefs {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetElementRefs(
      jsonObject.removed ? List(jsonObject.removed.map(json => ChangeSetElementRef.fromJSON(json))) : List(),
      jsonObject.added ? List(jsonObject.added.map(json => ChangeSetElementRef.fromJSON(json))) : List(),
      jsonObject.updated ? List(jsonObject.updated.map(json => ChangeSetElementRef.fromJSON(json))) : List()
    );
  }
}
