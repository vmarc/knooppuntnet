import {List} from "immutable";
import {KnownElements} from "../../../../kpn/api/common/common/known-elements";
import {Ref} from "../../../../kpn/api/common/common/ref";
import {RefDiffs} from "../../../../kpn/api/common/diff/ref-diffs";
import {NodeChangeInfo} from "../../../../kpn/api/common/node/node-change-info";

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
