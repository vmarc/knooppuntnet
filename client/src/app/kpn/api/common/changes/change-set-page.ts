// this class is generated, please do not modify

import {ChangeSetInfo} from './change-set-info';
import {ChangeSetSummary} from '../change-set-summary';
import {KnownElements} from '../common/known-elements';
import {NetworkChangeInfo} from './details/network-change-info';
import {NodeChangeInfo} from '../node/node-change-info';
import {RouteChangeInfo} from '../route/route-change-info';

export class ChangeSetPage {

  constructor(readonly summary: ChangeSetSummary,
              readonly changeSetInfo: ChangeSetInfo,
              readonly networkChanges: Array<NetworkChangeInfo>,
              readonly routeChanges: Array<RouteChangeInfo>,
              readonly nodeChanges: Array<NodeChangeInfo>,
              readonly knownElements: KnownElements) {
  }

  public static fromJSON(jsonObject: any): ChangeSetPage {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetPage(
      ChangeSetSummary.fromJSON(jsonObject.summary),
      ChangeSetInfo.fromJSON(jsonObject.changeSetInfo),
      jsonObject.networkChanges ? Array(jsonObject.networkChanges.map((json: any) => NetworkChangeInfo.fromJSON(json))) : Array(),
      jsonObject.routeChanges ? Array(jsonObject.routeChanges.map((json: any) => RouteChangeInfo.fromJSON(json))) : Array(),
      jsonObject.nodeChanges ? Array(jsonObject.nodeChanges.map((json: any) => NodeChangeInfo.fromJSON(json))) : Array(),
      KnownElements.fromJSON(jsonObject.knownElements)
    );
  }
}
