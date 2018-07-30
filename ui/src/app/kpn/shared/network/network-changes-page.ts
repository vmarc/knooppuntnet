// this class is generated, please do not modify

import {ChangesFilter} from '../changes/filter/changes-filter';
import {NetworkChangeInfo} from '../changes/details/network-change-info';
import {NetworkInfo} from './network-info';

export class NetworkChangesPage {

  constructor(public network?: NetworkInfo,
              public filter?: ChangesFilter,
              public changes?: Array<NetworkChangeInfo>,
              public totalCount?: number) {
  }

  public static fromJSON(jsonObject): NetworkChangesPage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkChangesPage();
    instance.network = jsonObject.network;
    instance.filter = jsonObject.filter;
    instance.changes = jsonObject.changes ? jsonObject.changes.map(json => NetworkChangeInfo.fromJSON(json)) : [];
    instance.totalCount = jsonObject.totalCount;
    return instance;
  }
}

