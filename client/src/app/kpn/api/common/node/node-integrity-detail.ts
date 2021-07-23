// this class is generated, please do not modify

import { List } from 'immutable';
import { NetworkType } from '../../custom/network-type';
import { Ref } from '../common/ref';
import {NetworkScope} from "@api/custom/network-scope";

export class NodeIntegrityDetail {
  constructor(
    readonly networkScope: NetworkScope,
    readonly networkType: NetworkType,
    readonly expectedRouteCount: number,
    readonly routeRefs: List<Ref>
  ) {}

  public static fromJSON(jsonObject: any): NodeIntegrityDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeIntegrityDetail(
      NetworkScope.fromJSON(jsonObject.networkScope),
      NetworkType.fromJSON(jsonObject.networkType),
      jsonObject.expectedRouteCount,
      jsonObject.routeRefs
        ? List(jsonObject.routeRefs.map((json: any) => Ref.fromJSON(json)))
        : List()
    );
  }
}
