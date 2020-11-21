// this class is generated, please do not modify

import {List} from 'immutable';
import {Ref} from './ref';

export class NodeRouteRefs {

  constructor(readonly nodeId: number,
              readonly routeRefs: List<Ref>) {
  }

  public static fromJSON(jsonObject: any): NodeRouteRefs {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeRouteRefs(
      jsonObject.nodeId,
      jsonObject.routeRefs ? List(jsonObject.routeRefs.map((json: any) => Ref.fromJSON(json))) : List()
    );
  }
}
