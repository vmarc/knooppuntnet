// this class is generated, please do not modify

import {List} from 'immutable';
import {NodeInfo} from '../node-info';
import {SubsetInfo} from './subset-info';
import {TimeInfo} from '../time-info';

export class SubsetOrphanNodesPage {
  readonly timeInfo: TimeInfo;
  readonly subsetInfo: SubsetInfo;
  readonly rows: List<NodeInfo>;

  constructor(timeInfo: TimeInfo,
              subsetInfo: SubsetInfo,
              rows: List<NodeInfo>) {
    this.timeInfo = timeInfo;
    this.subsetInfo = subsetInfo;
    this.rows = rows;
  }

  public static fromJSON(jsonObject): SubsetOrphanNodesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetOrphanNodesPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      SubsetInfo.fromJSON(jsonObject.subsetInfo),
      jsonObject.rows ? List(jsonObject.rows.map(json => NodeInfo.fromJSON(json))) : List()
    );
  }
}
