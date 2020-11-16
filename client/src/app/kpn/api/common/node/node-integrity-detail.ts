// this class is generated, please do not modify

import {List} from 'immutable';
import {NetworkType} from '../../custom/network-type';
import {Ref} from '../common/ref';

export class NodeIntegrityDetail {

  constructor(readonly networkType: NetworkType,
              readonly expectedRouteCount: number,
              readonly routeRefs: List<Ref>) {
  }

  public static fromJSON(jsonObject: any): NodeIntegrityDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeIntegrityDetail(
      NetworkType.fromJSON(jsonObject.networkType),
      jsonObject.expectedRouteCount,
      jsonObject.routeRefs ? List(jsonObject.routeRefs.map((json: any) => Ref.fromJSON(json))) : List()
    );
  }
}
