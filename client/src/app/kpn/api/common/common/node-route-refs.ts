// this class is generated, please do not modify

import {Ref} from './ref';

export class NodeRouteRefs {

  constructor(readonly nodeId: number,
              readonly routeRefs: Array<Ref>) {
  }

  public static fromJSON(jsonObject: any): NodeRouteRefs {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeRouteRefs(
      jsonObject.nodeId,
      jsonObject.routeRefs.map((json: any) => Ref.fromJSON(json))
    );
  }
}
