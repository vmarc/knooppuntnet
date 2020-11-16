// this class is generated, please do not modify

import {ScopedNetworkType} from '../custom/scoped-network-type';

export class NodeName {

  constructor(readonly scopedNetworkType: ScopedNetworkType,
              readonly name: string) {
  }

  public static fromJSON(jsonObject: any): NodeName {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeName(
      ScopedNetworkType.fromJSON(jsonObject.scopedNetworkType),
      jsonObject.name
    );
  }
}
