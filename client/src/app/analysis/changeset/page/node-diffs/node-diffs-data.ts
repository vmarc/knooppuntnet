import {List} from 'immutable';
import {KnownElements} from '@api/common/common/known-elements';
import {Ref} from '@api/common/common/ref';
import {RefDiffs} from '@api/common/diff/ref-diffs';
import {NodeChangeInfo} from '@api/common/node/node-change-info';

export class NodeDiffsData {

  constructor(readonly refDiffs: RefDiffs,
              readonly changeSetId: number,
              readonly knownElements: KnownElements,
              readonly nodeChangeInfos: List<NodeChangeInfo>) {
  }

  findNodeChangeInfo(ref: Ref) {
    return this.nodeChangeInfos.filter(node => node.id === ref.id);
  }

}
