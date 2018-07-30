// this class is generated, please do not modify

import {Fact} from '../fact';
import {NetworkRoutesFacts} from './network-routes-facts';
import {SubsetInfo} from './subset-info';

export class SubsetFactDetailsPage {

  constructor(public subsetInfo?: SubsetInfo,
              public fact?: Fact,
              public networks?: Array<NetworkRoutesFacts>) {
  }

  public static fromJSON(jsonObject): SubsetFactDetailsPage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new SubsetFactDetailsPage();
    instance.subsetInfo = jsonObject.subsetInfo;
    instance.fact = jsonObject.fact;
    instance.networks = jsonObject.networks ? jsonObject.networks.map(json => NetworkRoutesFacts.fromJSON(json)) : [];
    return instance;
  }
}

