// this class is generated, please do not modify

import {NodeInfo} from '../node-info';
import {SubsetInfo} from './subset-info';
import {TimeInfo} from '../time-info';

export class SubsetOrphanNodesPage {

  constructor(public timeInfo?: TimeInfo,
              public subsetInfo?: SubsetInfo,
              public rows?: Array<NodeInfo>) {
  }

  public static fromJSON(jsonObject): SubsetOrphanNodesPage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new SubsetOrphanNodesPage();
    instance.timeInfo = jsonObject.timeInfo;
    instance.subsetInfo = jsonObject.subsetInfo;
    instance.rows = jsonObject.rows ? jsonObject.rows.map(json => NodeInfo.fromJSON(json)) : [];
    return instance;
  }
}

