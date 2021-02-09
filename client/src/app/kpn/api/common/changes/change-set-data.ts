// this class is generated, please do not modify

import {ChangeSetSummary} from '../change-set-summary';
import {NetworkChange} from './details/network-change';
import {NodeChange} from './details/node-change';
import {RouteChange} from './details/route-change';

export class ChangeSetData {

  constructor(readonly summary: ChangeSetSummary,
              readonly networkChanges: Array<NetworkChange>,
              readonly routeChanges: Array<RouteChange>,
              readonly nodeChanges: Array<NodeChange>) {
  }

  public static fromJSON(jsonObject: any): ChangeSetData {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetData(
      ChangeSetSummary.fromJSON(jsonObject.summary),
      jsonObject.networkChanges ? Array(jsonObject.networkChanges.map((json: any) => NetworkChange.fromJSON(json))) : Array(),
      jsonObject.routeChanges ? Array(jsonObject.routeChanges.map((json: any) => RouteChange.fromJSON(json))) : Array(),
      jsonObject.nodeChanges ? Array(jsonObject.nodeChanges.map((json: any) => NodeChange.fromJSON(json))) : Array()
    );
  }
}
