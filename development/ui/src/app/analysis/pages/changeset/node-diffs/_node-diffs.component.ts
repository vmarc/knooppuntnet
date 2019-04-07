import {Component, Input} from '@angular/core';
import {List} from "immutable";
import {RefDiffs} from "../../../../kpn/shared/diff/ref-diffs";
import {NodeChangeInfo} from "../../../../kpn/shared/node/node-change-info";

@Component({
  selector: 'kpn-node-diffs',
  template: `

    <kpn-node-diffs-removed
      [changeSetId]="changeSetId"
      [nodeRefs]="nodeDiffs.removed"
      [nodeChangeInfos]="nodeChangeInfos">
    </kpn-node-diffs-removed>

    <kpn-node-diffs-added
      [changeSetId]="changeSetId"
      [nodeRefs]="nodeDiffs.added"
      [nodeChangeInfos]="nodeChangeInfos">
    </kpn-node-diffs-added>

    <kpn-node-diffs-updated
      [changeSetId]="changeSetId"
      [nodeRefs]="nodeDiffs.updated"
      [nodeChangeInfos]="nodeChangeInfos">
    </kpn-node-diffs-updated>
  `
})
export class NodeDiffsComponent {

  @Input() changeSetId: number;
  @Input() nodeDiffs: RefDiffs;
  @Input() nodeChangeInfos: List<NodeChangeInfo>;

  findNodeChangeInfo(nodeId: number): List<NodeChangeInfo> {
    return this.nodeChangeInfos.filter(n => n.id === nodeId);
  }

}
