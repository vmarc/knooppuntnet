// this class is generated, please do not modify

import {ChangeSetSummary} from '../change-set-summary';
import {NetworkChange} from './details/network-change';
import {NodeChange} from './details/node-change';
import {RouteChange} from './details/route-change';

export class ChangeSetData {

  constructor(public summary?: ChangeSetSummary,
              public networkChanges?: Array<NetworkChange>,
              public routeChanges?: Array<RouteChange>,
              public nodeChanges?: Array<NodeChange>) {
  }

  public static fromJSON(jsonObject): ChangeSetData {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangeSetData();
    instance.summary = ChangeSetSummary.fromJSON(jsonObject.summary);
    instance.networkChanges = jsonObject.networkChanges ? jsonObject.networkChanges.map(json => NetworkChange.fromJSON(json)) : [];
    instance.routeChanges = jsonObject.routeChanges ? jsonObject.routeChanges.map(json => RouteChange.fromJSON(json)) : [];
    instance.nodeChanges = jsonObject.nodeChanges ? jsonObject.nodeChanges.map(json => NodeChange.fromJSON(json)) : [];
    return instance;
  }
}

