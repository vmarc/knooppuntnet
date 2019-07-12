import {Component, Input} from "@angular/core";
import {NodeChangeInfo} from "../../../../kpn/shared/node/node-change-info";

@Component({
  selector: "kpn-node-change-moved",
  template: `

    <div *ngIf="!!nodeMoved">

      <div *ngIf="nodeMoved.distance > 0" class="kpn-detail">
        The node has moved {{nodeMoved.distance}} meters.
        <!--@@ Het knooppunt is {nodeMoved.distance} meter verplaatst. -->
      </div>

      <div *ngIf="nodeMoved.distance == 0" class="kpn-detail">
        The node has moved less than 1 meter.
        <!--@@ Het knooppunt is minder dan 1 meter verplaatst. -->
      </div>

      <kpn-node-moved-map [nodeMoved]="nodeMoved"></kpn-node-moved-map>

      <div class="note">
        Note: Node position is shown as it was at
        <kpn-timestamp [timestamp]="nodeChangeInfo.changeKey.timestamp"></kpn-timestamp>
        ,
        while the map background is shown as it is today.

        <!--@@
        s"Noot: De knooppunt positie wordt getoond zoals die was op {nodeChange.changeKey.timestamp.yyyymmddhhmm}, de kaart achtergrond toont " +
        s"de toestand van vandaag."
        -->

      </div>

    </div>
  `
})
export class NodeChangeMovedComponent {

  @Input() nodeChangeInfo: NodeChangeInfo;

  get nodeMoved() {
    return this.nodeChangeInfo.nodeMoved;
  }

}
