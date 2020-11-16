// this class is generated, please do not modify

import {List} from 'immutable';
import {Ref} from '../../common/ref';

export class RouteNodeDiff {

  constructor(readonly title: string,
              readonly added: List<Ref>,
              readonly removed: List<Ref>) {
  }

  public static fromJSON(jsonObject: any): RouteNodeDiff {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteNodeDiff(
      jsonObject.title,
      jsonObject.added ? List(jsonObject.added.map((json: any) => Ref.fromJSON(json))) : List(),
      jsonObject.removed ? List(jsonObject.removed.map((json: any) => Ref.fromJSON(json))) : List()
    );
  }
}
