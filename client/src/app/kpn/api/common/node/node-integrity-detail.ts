// this class is generated, please do not modify

import { NetworkScope } from '../../custom/network-scope';
import { NetworkType } from '../../custom/network-type';
import { Ref } from '../common/ref';

export class NodeIntegrityDetail {
  constructor(
    readonly networkType: NetworkType,
    readonly networkScope: NetworkScope,
    readonly expectedRouteCount: number,
    readonly routeRefs: Array<Ref>
  ) {}

  static fromJSON(jsonObject: any): NodeIntegrityDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeIntegrityDetail(
      jsonObject.networkType,
      jsonObject.networkScope,
      jsonObject.expectedRouteCount,
      jsonObject.routeRefs.map((json: any) => Ref.fromJSON(json))
    );
  }
}
