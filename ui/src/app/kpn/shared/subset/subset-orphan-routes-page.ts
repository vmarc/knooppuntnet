// this class is generated, please do not modify

import {RouteSummary} from '../route-summary';
import {SubsetInfo} from './subset-info';
import {TimeInfo} from '../time-info';

export class SubsetOrphanRoutesPage {

  constructor(public timeInfo?: TimeInfo,
              public subsetInfo?: SubsetInfo,
              public rows?: Array<RouteSummary>) {
  }

  public static fromJSON(jsonObject): SubsetOrphanRoutesPage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new SubsetOrphanRoutesPage();
    instance.timeInfo = TimeInfo.fromJSON(jsonObject.timeInfo);
    instance.subsetInfo = SubsetInfo.fromJSON(jsonObject.subsetInfo);
    instance.rows = jsonObject.rows ? jsonObject.rows.map(json => RouteSummary.fromJSON(json)) : [];
    return instance;
  }
}

