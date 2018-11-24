// this class is generated, please do not modify

import {List} from 'immutable';
import {RouteSummary} from '../route-summary';
import {SubsetInfo} from './subset-info';
import {TimeInfo} from '../time-info';

export class SubsetOrphanRoutesPage {
  readonly timeInfo: TimeInfo;
  readonly subsetInfo: SubsetInfo;
  readonly rows: List<RouteSummary>;

  constructor(timeInfo: TimeInfo,
              subsetInfo: SubsetInfo,
              rows: List<RouteSummary>) {
    this.timeInfo = timeInfo;
    this.subsetInfo = subsetInfo;
    this.rows = rows;
  }

  public static fromJSON(jsonObject): SubsetOrphanRoutesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetOrphanRoutesPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      SubsetInfo.fromJSON(jsonObject.subsetInfo),
      jsonObject.rows ? List(jsonObject.rows.map(json => RouteSummary.fromJSON(json))) : List()
    );
  }
}
