// this class is generated, please do not modify

import {List} from 'immutable';
import {ChangesFilter} from '../changes/filter/changes-filter';
import {NetworkChangeInfo} from '../changes/details/network-change-info';
import {NetworkInfo} from './network-info';

export class NetworkChangesPage {
  readonly network: NetworkInfo;
  readonly filter: ChangesFilter;
  readonly changes: List<NetworkChangeInfo>;
  readonly totalCount: number;

  constructor(network: NetworkInfo,
              filter: ChangesFilter,
              changes: List<NetworkChangeInfo>,
              totalCount: number) {
    this.network = network;
    this.filter = filter;
    this.changes = changes;
    this.totalCount = totalCount;
  }

  public static fromJSON(jsonObject): NetworkChangesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkChangesPage(
      NetworkInfo.fromJSON(jsonObject.network),
      ChangesFilter.fromJSON(jsonObject.filter),
      jsonObject.changes ? List(jsonObject.changes.map(json => NetworkChangeInfo.fromJSON(json))) : List(),
      jsonObject.totalCount
    );
  }
}
