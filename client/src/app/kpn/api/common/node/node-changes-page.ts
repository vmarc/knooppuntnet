// this class is generated, please do not modify

import {List} from 'immutable';
import {ChangesFilter} from '../changes/filter/changes-filter';
import {NodeChangeInfo} from './node-change-info';
import {NodeInfo} from '../node-info';

export class NodeChangesPage {

  constructor(readonly nodeInfo: NodeInfo,
              readonly filter: ChangesFilter,
              readonly changes: List<NodeChangeInfo>,
              readonly incompleteWarning: boolean,
              readonly totalCount: number,
              readonly changeCount: number) {
  }

  static fromJSON(jsonObject: any): NodeChangesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeChangesPage(
      NodeInfo.fromJSON(jsonObject.nodeInfo),
      ChangesFilter.fromJSON(jsonObject.filter),
      jsonObject.changes ? List(jsonObject.changes.map((json: any) => NodeChangeInfo.fromJSON(json))) : List(),
      jsonObject.incompleteWarning,
      jsonObject.totalCount,
      jsonObject.changeCount
    );
  }
}
