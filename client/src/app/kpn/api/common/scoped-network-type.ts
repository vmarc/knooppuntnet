// this class is generated, please do not modify

import {NetworkScope} from "../custom/network-scope";
import {NetworkType} from "../custom/network-type";

export class ScopedNetworkType {

  constructor(readonly networkScope: NetworkScope,
              readonly networkType: NetworkType,
              readonly key: string) {
  }

  public static fromJSON(jsonObject): ScopedNetworkType {
    if (!jsonObject) {
      return undefined;
    }
    return new ScopedNetworkType(
      NetworkScope.fromJSON(jsonObject.networkScope),
      NetworkType.fromJSON(jsonObject.networkType),
      jsonObject.key
    );
  }
}
