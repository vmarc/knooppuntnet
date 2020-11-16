// this class is generated, please do not modify

import {List} from 'immutable';
import {ChangeSetSummary} from '../change-set-summary';
import {NetworkChange} from './details/network-change';
import {NodeChange} from './details/node-change';
import {RouteChange} from './details/route-change';

export class ChangeSetData {

  constructor(readonly summary: ChangeSetSummary,
              readonly networkChanges: List<NetworkChange>,
              readonly routeChanges: List<RouteChange>,
              readonly nodeChanges: List<NodeChange>) {
  }

  public static fromJSON(jsonObject: any): ChangeSetData {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetData(
      ChangeSetSummary.fromJSON(jsonObject.summary),
      jsonObject.networkChanges ? List(jsonObject.networkChanges.map((json: any) => NetworkChange.fromJSON(json))) : List(),
      jsonObject.routeChanges ? List(jsonObject.routeChanges.map((json: any) => RouteChange.fromJSON(json))) : List(),
      jsonObject.nodeChanges ? List(jsonObject.nodeChanges.map((json: any) => NodeChange.fromJSON(json))) : List()
    );
  }
}
