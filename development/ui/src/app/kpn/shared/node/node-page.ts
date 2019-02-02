// this class is generated, please do not modify

import {NodeChangeInfos} from './node-change-infos';
import {NodeInfo} from '../node-info';
import {NodeReferences} from './node-references';

export class NodePage {

  constructor(readonly nodeInfo: NodeInfo,
              readonly references: NodeReferences,
              readonly nodeChanges: NodeChangeInfos) {
  }

  public static fromJSON(jsonObject): NodePage {
    if (!jsonObject) {
      return undefined;
    }
    return new NodePage(
      NodeInfo.fromJSON(jsonObject.nodeInfo),
      NodeReferences.fromJSON(jsonObject.references),
      NodeChangeInfos.fromJSON(jsonObject.nodeChanges)
    );
  }
}
