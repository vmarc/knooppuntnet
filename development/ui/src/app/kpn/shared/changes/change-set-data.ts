// this class is generated, please do not modify

import {List} from 'immutable';
import {ChangeSetSummary} from '../change-set-summary';
import {NetworkChange} from './details/network-change';
import {NodeChange} from './details/node-change';
import {RouteChange} from './details/route-change';

export class ChangeSetData {
  readonly summary: ChangeSetSummary;
  readonly networkChanges: List<NetworkChange>;
  readonly routeChanges: List<RouteChange>;
  readonly nodeChanges: List<NodeChange>;

  constructor(summary: ChangeSetSummary,
              networkChanges: List<NetworkChange>,
              routeChanges: List<RouteChange>,
              nodeChanges: List<NodeChange>) {
    this.summary = summary;
    this.networkChanges = networkChanges;
    this.routeChanges = routeChanges;
    this.nodeChanges = nodeChanges;
  }

  public static fromJSON(jsonObject): ChangeSetData {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetData(
      ChangeSetSummary.fromJSON(jsonObject.summary),
      jsonObject.networkChanges ? List(jsonObject.networkChanges.map(json => NetworkChange.fromJSON(json))) : List(),
      jsonObject.routeChanges ? List(jsonObject.routeChanges.map(json => RouteChange.fromJSON(json))) : List(),
      jsonObject.nodeChanges ? List(jsonObject.nodeChanges.map(json => NodeChange.fromJSON(json))) : List()
    );
  }
}
