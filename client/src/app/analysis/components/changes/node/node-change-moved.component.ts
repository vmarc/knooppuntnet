import {Component, Input} from "@angular/core";
import {NodeChangeInfo} from "../../../../kpn/api/common/node/node-change-info";

@Component({
  selector: "kpn-node-change-moved",
  template: `

    <div *ngIf="!!nodeMoved">

      <div *ngIf="nodeMoved.distance > 0" class="kpn-detail" i18n="@@node-change.moved.meters">
        The node has moved {{nodeMoved.distance}} meters.
      </div>

      <div *ngIf="nodeMoved.distance == 0" class="kpn-detail" i18n="@@node-change.moved.less-than-1-meter">
        The node has moved less than 1 meter.
      </div>

      <kpn-node-moved-map [nodeMoved]="nodeMoved"></kpn-node-moved-map>

      <div class="note">
        <span i18n="@@node-change.moved.note.1">
          Note: Node position is shown as it was at
        </span>
        <kpn-timestamp [timestamp]="nodeChangeInfo.changeKey.timestamp"></kpn-timestamp>
        <span i18n="@@node-change.moved.note.2">
          , while the map background is shown as it is today.
        </span>
      </div>

    </div>
  `,
  styles: [`
    .note {
      margin-top: 5px;
    }
  `]
})
export class NodeChangeMovedComponent {

  @Input() nodeChangeInfo: NodeChangeInfo;

  get nodeMoved() {
    return this.nodeChangeInfo.nodeMoved;
  }

}
