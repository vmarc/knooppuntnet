// this class is generated, please do not modify

import { ChangeSetSummary } from '../change-set-summary';
import { NetworkChange } from './details/network-change';
import { NodeChange } from './details/node-change';
import { RouteChange } from './details/route-change';

export class ChangeSetData {
  constructor(
    readonly summary: ChangeSetSummary,
    readonly networkChanges: Array<NetworkChange>,
    readonly routeChanges: Array<RouteChange>,
    readonly nodeChanges: Array<NodeChange>
  ) {}

  static fromJSON(jsonObject: any): ChangeSetData {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetData(
      ChangeSetSummary.fromJSON(jsonObject.summary),
      jsonObject.networkChanges?.map((json: any) =>
        NetworkChange.fromJSON(json)
      ),
      jsonObject.routeChanges?.map((json: any) => RouteChange.fromJSON(json)),
      jsonObject.nodeChanges?.map((json: any) => NodeChange.fromJSON(json))
    );
  }
}
