import {Component, Input} from "@angular/core";
import {NodeChangeInfo} from "../../../kpn/shared/node/node-change-info";

@Component({
  selector: "kpn-node-change",
  template: `

    <kpn-change-header
      [changeKey]="nodeChangeInfo.changeKey"
      [happy]="nodeChangeInfo.happy"
      [investigate]="nodeChangeInfo.investigate"
      [comment]="nodeChangeInfo.comment">
    </kpn-change-header>

    <kpn-change-set-info [changeSetTags]="nodeChangeInfo.changeTags"></kpn-change-set-info>

    <div class="kpn-detail">
      Version {{nodeChangeInfo.version}} <span *ngIf="isVersionUnchanged()">(Unchanged)</span>
    </div>

    <kpn-node-change-detail [nodeChangeInfo]="nodeChangeInfo"></kpn-node-change-detail>

  `
})
export class NodeChangeComponent {

  @Input() nodeChangeInfo: NodeChangeInfo;

  isVersionUnchanged(): boolean {
    const before = this.nodeChangeInfo.before ? this.nodeChangeInfo.before.version : null;
    const after = this.nodeChangeInfo.after ? this.nodeChangeInfo.after.version : null;
    return before && after && before == after;
  }

}
