// this class is generated, please do not modify

import {List} from 'immutable';
import {Ref} from '../../common/ref';

export class RouteNodeDiff {
  readonly title: string;
  readonly added: List<Ref>;
  readonly removed: List<Ref>;

  constructor(title: string,
              added: List<Ref>,
              removed: List<Ref>) {
    this.title = title;
    this.added = added;
    this.removed = removed;
  }

  public static fromJSON(jsonObject): RouteNodeDiff {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteNodeDiff(
      jsonObject.title,
      jsonObject.added ? List(jsonObject.added.map(json => Ref.fromJSON(json))) : List(),
      jsonObject.removed ? List(jsonObject.removed.map(json => Ref.fromJSON(json))) : List()
    );
  }
}
