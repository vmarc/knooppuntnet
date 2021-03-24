// this class is generated, please do not modify

import {NetworkType} from '../../custom/network-type';

export class NodeOrphanRouteReference {

  constructor(readonly networkType: NetworkType,
              readonly routeId: number,
              readonly routeName: string) {
  }

  static fromJSON(jsonObject: any): NodeOrphanRouteReference {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeOrphanRouteReference(
      jsonObject.networkType,
      jsonObject.routeId,
      jsonObject.routeName
    );
  }
}
