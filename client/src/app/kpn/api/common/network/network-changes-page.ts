// this class is generated, please do not modify

import {List} from 'immutable';
import {ChangesFilter} from '../changes/filter/changes-filter';
import {NetworkChangeInfo} from '../changes/details/network-change-info';
import {NetworkSummary} from './network-summary';

export class NetworkChangesPage {

  constructor(readonly network: NetworkSummary,
              readonly filter: ChangesFilter,
              readonly changes: List<NetworkChangeInfo>,
              readonly totalCount: number) {
  }

  public static fromJSON(jsonObject: any): NetworkChangesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkChangesPage(
      NetworkSummary.fromJSON(jsonObject.network),
      ChangesFilter.fromJSON(jsonObject.filter),
      jsonObject.changes ? List(jsonObject.changes.map((json: any) => NetworkChangeInfo.fromJSON(json))) : List(),
      jsonObject.totalCount
    );
  }
}
