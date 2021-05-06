// this class is generated, please do not modify

import { OrphanNodeInfo } from '../orphan-node-info';
import { TimeInfo } from '../time-info';
import { SubsetInfo } from './subset-info';

export class SubsetOrphanNodesPage {
  constructor(
    readonly timeInfo: TimeInfo,
    readonly subsetInfo: SubsetInfo,
    readonly nodes: Array<OrphanNodeInfo>
  ) {}

  static fromJSON(jsonObject: any): SubsetOrphanNodesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetOrphanNodesPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      SubsetInfo.fromJSON(jsonObject.subsetInfo),
      jsonObject.nodes.map((json: any) => OrphanNodeInfo.fromJSON(json))
    );
  }
}
