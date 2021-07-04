// this class is generated, please do not modify

import { NodeInfo } from '../node-info';
import { NodeIntegrity } from './node-integrity';
import { NodeReferences } from './node-references';

export class NodeDetailsPage {
  constructor(
    readonly nodeInfo: NodeInfo,
    readonly references: NodeReferences,
    readonly integrity: NodeIntegrity,
    readonly changeCount: number
  ) {}

  public static fromJSON(jsonObject: any): NodeDetailsPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeDetailsPage(
      NodeInfo.fromJSON(jsonObject.nodeInfo),
      NodeReferences.fromJSON(jsonObject.references),
      NodeIntegrity.fromJSON(jsonObject.integrity),
      jsonObject.changeCount
    );
  }
}
