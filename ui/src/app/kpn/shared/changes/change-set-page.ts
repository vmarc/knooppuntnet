// this class is generated, please do not modify

import {ChangeSetInfo} from './change-set-info';
import {ChangeSetSummary} from '../change-set-summary';
import {KnownElements} from '../common/known-elements';
import {NetworkChangeInfo} from './details/network-change-info';
import {NodeChangeInfo} from '../node/node-change-info';
import {Review} from './review';
import {RouteChangeInfo} from '../route/route-change-info';

export class ChangeSetPage {

  constructor(public summary?: ChangeSetSummary,
              public changeSetInfo?: ChangeSetInfo,
              public networkChanges?: Array<NetworkChangeInfo>,
              public routeChanges?: Array<RouteChangeInfo>,
              public nodeChanges?: Array<NodeChangeInfo>,
              public knownElements?: KnownElements,
              public reviews?: Array<Review>) {
  }

  public static fromJSON(jsonObject): ChangeSetPage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangeSetPage();
    instance.summary = jsonObject.summary;
    instance.changeSetInfo = jsonObject.changeSetInfo;
    instance.networkChanges = jsonObject.networkChanges ? jsonObject.networkChanges.map(json => NetworkChangeInfo.fromJSON(json)) : [];
    instance.routeChanges = jsonObject.routeChanges ? jsonObject.routeChanges.map(json => RouteChangeInfo.fromJSON(json)) : [];
    instance.nodeChanges = jsonObject.nodeChanges ? jsonObject.nodeChanges.map(json => NodeChangeInfo.fromJSON(json)) : [];
    instance.knownElements = jsonObject.knownElements;
    instance.reviews = jsonObject.reviews ? jsonObject.reviews.map(json => Review.fromJSON(json)) : [];
    return instance;
  }
}

