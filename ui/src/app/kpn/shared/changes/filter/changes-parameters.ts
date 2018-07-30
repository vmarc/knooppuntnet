// this class is generated, please do not modify

import {Subset} from '../../subset';

export class ChangesParameters {

  constructor(public subset?: Subset,
              public networkId?: number,
              public routeId?: number,
              public nodeId?: number,
              public year?: string,
              public month?: string,
              public day?: string,
              public itemsPerPage?: number,
              public pageIndex?: number,
              public impact?: boolean) {
  }

  public static fromJSON(jsonObject): ChangesParameters {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangesParameters();
    instance.subset = jsonObject.subset;
    instance.networkId = jsonObject.networkId;
    instance.routeId = jsonObject.routeId;
    instance.nodeId = jsonObject.nodeId;
    instance.year = jsonObject.year;
    instance.month = jsonObject.month;
    instance.day = jsonObject.day;
    instance.itemsPerPage = jsonObject.itemsPerPage;
    instance.pageIndex = jsonObject.pageIndex;
    instance.impact = jsonObject.impact;
    return instance;
  }
}

