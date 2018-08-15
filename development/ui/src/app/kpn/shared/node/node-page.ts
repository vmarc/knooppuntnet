// this class is generated, please do not modify

import {NodeChangeInfos} from './node-change-infos';
import {NodeInfo} from '../node-info';
import {NodeReferences} from './node-references';

export class NodePage {

  constructor(public nodeInfo?: NodeInfo,
              public references?: NodeReferences,
              public nodeChanges?: NodeChangeInfos) {
  }

  public static fromJSON(jsonObject): NodePage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodePage();
    instance.nodeInfo = NodeInfo.fromJSON(jsonObject.nodeInfo);
    instance.references = NodeReferences.fromJSON(jsonObject.references);
    instance.nodeChanges = NodeChangeInfos.fromJSON(jsonObject.nodeChanges);
    return instance;
  }
}

