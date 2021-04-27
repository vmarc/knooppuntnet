// this class is generated, please do not modify

import { NetworkScope } from '../custom/network-scope';
import { NetworkType } from '../custom/network-type';

export class NodeName {
  constructor(
    readonly networkType: NetworkType,
    readonly networkScope: NetworkScope,
    readonly name: string
  ) {}

  static fromJSON(jsonObject: any): NodeName {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeName(
      jsonObject.networkType,
      NetworkScope.fromJSON(jsonObject.networkScope),
      jsonObject.name
    );
  }
}
