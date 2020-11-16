// this class is generated, please do not modify

import {List} from 'immutable';
import {NodeInfo} from '../node-info';
import {SubsetInfo} from './subset-info';
import {TimeInfo} from '../time-info';

export class SubsetOrphanNodesPage {

  constructor(readonly timeInfo: TimeInfo,
              readonly subsetInfo: SubsetInfo,
              readonly rows: List<NodeInfo>) {
  }

  public static fromJSON(jsonObject: any): SubsetOrphanNodesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetOrphanNodesPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      SubsetInfo.fromJSON(jsonObject.subsetInfo),
      jsonObject.rows ? List(jsonObject.rows.map((json: any) => NodeInfo.fromJSON(json))) : List()
    );
  }
}
