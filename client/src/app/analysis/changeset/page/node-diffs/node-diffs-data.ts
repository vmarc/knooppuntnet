import { KnownElements } from '@api/common/common';
import { Ref } from '@api/common/common';
import { RefDiffs } from '@api/common/diff';
import { NodeChangeInfo } from '@api/common/node';
import { List } from 'immutable';

export class NodeDiffsData {
  constructor(
    readonly refDiffs: RefDiffs,
    readonly changeSetId: number,
    readonly knownElements: KnownElements,
    readonly nodeChangeInfos: List<NodeChangeInfo>
  ) {}

  findNodeChangeInfo(ref: Ref) {
    return this.nodeChangeInfos.filter((node) => node.id === ref.id);
  }
}
