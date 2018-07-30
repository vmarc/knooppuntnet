// this class is generated, please do not modify

import {ChangeSetElementRefs} from './change-set-element-refs';
import {Country} from './country';
import {NetworkType} from './network-type';

export class ChangeSetNetwork {

  constructor(public country?: Country,
              public networkType?: NetworkType,
              public networkId?: number,
              public networkName?: string,
              public routeChanges?: ChangeSetElementRefs,
              public nodeChanges?: ChangeSetElementRefs,
              public happy?: boolean,
              public investigate?: boolean) {
  }

  public static fromJSON(jsonObject): ChangeSetNetwork {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangeSetNetwork();
    instance.country = jsonObject.country;
    instance.networkType = jsonObject.networkType;
    instance.networkId = jsonObject.networkId;
    instance.networkName = jsonObject.networkName;
    instance.routeChanges = jsonObject.routeChanges;
    instance.nodeChanges = jsonObject.nodeChanges;
    instance.happy = jsonObject.happy;
    instance.investigate = jsonObject.investigate;
    return instance;
  }
}

