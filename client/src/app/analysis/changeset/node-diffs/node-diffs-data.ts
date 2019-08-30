import {List} from "immutable";
import {KnownElements} from "../../../kpn/shared/common/known-elements";
import {Ref} from "../../../kpn/shared/common/ref";
import {RefDiffs} from "../../../kpn/shared/diff/ref-diffs";
import {NodeChangeInfo} from "../../../kpn/shared/node/node-change-info";

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
