// this class is generated, please do not modify

import { NetworkType } from '../../custom/network-type';
import { Ref } from '../common/ref';

export class NodeIntegrityDetail {
  constructor(
    readonly networkType: NetworkType,
    readonly expectedRouteCount: number,
    readonly routeRefs: Array<Ref>
  ) {}

  public static fromJSON(jsonObject: any): NodeIntegrityDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeIntegrityDetail(
      jsonObject.networkType,
      jsonObject.expectedRouteCount,
      jsonObject.routeRefs.map((json: any) => Ref.fromJSON(json))
    );
  }
}
