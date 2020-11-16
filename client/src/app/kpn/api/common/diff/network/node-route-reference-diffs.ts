// this class is generated, please do not modify

import {List} from 'immutable';
import {Ref} from '../../common/ref';

export class NodeRouteReferenceDiffs {

  constructor(readonly removed: List<Ref>,
              readonly added: List<Ref>,
              readonly remaining: List<Ref>) {
  }

  public static fromJSON(jsonObject: any): NodeRouteReferenceDiffs {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeRouteReferenceDiffs(
      jsonObject.removed ? List(jsonObject.removed.map((json: any) => Ref.fromJSON(json))) : List(),
      jsonObject.added ? List(jsonObject.added.map((json: any) => Ref.fromJSON(json))) : List(),
      jsonObject.remaining ? List(jsonObject.remaining.map((json: any) => Ref.fromJSON(json))) : List()
    );
  }
}
