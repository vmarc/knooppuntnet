// this class is generated, please do not modify

import {List} from 'immutable';
import {Ref} from '../../common/ref';

export class RefChanges {
  readonly oldRefs: List<Ref>;
  readonly newRefs: List<Ref>;

  constructor(oldRefs: List<Ref>,
              newRefs: List<Ref>) {
    this.oldRefs = oldRefs;
    this.newRefs = newRefs;
  }

  public static fromJSON(jsonObject): RefChanges {
    if (!jsonObject) {
      return undefined;
    }
    return new RefChanges(
      jsonObject.oldRefs ? List(jsonObject.oldRefs.map(json => Ref.fromJSON(json))) : List(),
      jsonObject.newRefs ? List(jsonObject.newRefs.map(json => Ref.fromJSON(json))) : List()
    );
  }
}
