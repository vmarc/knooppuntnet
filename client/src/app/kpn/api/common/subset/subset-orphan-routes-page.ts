// this class is generated, please do not modify

import {List} from 'immutable';
import {RouteSummary} from '../route-summary';
import {TimeInfo} from '../time-info';
import {SubsetInfo} from './subset-info';

export class SubsetOrphanRoutesPage {

  constructor(readonly timeInfo: TimeInfo,
              readonly subsetInfo: SubsetInfo,
              readonly rows: List<RouteSummary>) {
  }

  public static fromJSON(jsonObject: any): SubsetOrphanRoutesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetOrphanRoutesPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      SubsetInfo.fromJSON(jsonObject.subsetInfo),
      jsonObject.rows ? List(jsonObject.rows.map((json: any) => RouteSummary.fromJSON(json))) : List()
    );
  }
}
