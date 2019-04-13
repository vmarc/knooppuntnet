import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";

@Component({
  selector: "kpn-network-indicator-dialog",
  template: `
    <kpn-indicator-dialog letter="N" color="{{color}}" (closeDialog)="onCloseDialog()">

      <span dialog-title *ngIf="isOrange()">
        Unexpected - Defined in network relation        
      </span>
      <div dialog-body *ngIf="isOrange()">
        This node is included as a member in the network relation. We did not expect this,
        because all routes to this node have role "connection". This would mean that the
        node is part of another network. We expect that the node is not included in the
        network relation, unless it receives the role "connection".
      </div>

      <span dialog-title *ngIf="isGreen()">
        OK - Defined in network relation
      </span>
      <div dialog-body *ngIf="isGreen()">
        This node is included as a member in the network relation. This is what we expect.
      </div>

      <span dialog-title *ngIf="isGray()">
        OK - Not defined in network relation
      </span>
      <div dialog-body *ngIf="isGray()">
        This node is not included as a member in the network relation. This is OK. This node
        must belong to a different network, because all routes to this node within this network
        have the role "connection" in the network relation.
      </div>

      <span dialog-title *ngIf="isRed()">
        NOK - Not defined in network relation        
      </span>
      <div dialog-body *ngIf="isRed()">
        This node is not included as a member in the network relation. This is not OK. The
        convention is to include each node in the network relation. An exception is when the node
        belongs to another network (all routes to this node have role "connection" in
        the network relation), then the node does not have to be included as member in
        the network relation. The node can be added the network relation, but should get
        the role "connection" in that case.
      </div>

    </kpn-indicator-dialog>
  `,
  styles: []
})
export class NetworkIndicatorDialogComponent {

  constructor(public dialogRef: MatDialogRef<NetworkIndicatorDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public color: string) {
  }

  onCloseDialog(): void {
    this.dialogRef.close();
  }

  isOrange() {
    return this.color === "orange";
  }

  isGreen() {
    return this.color === "green";
  }

  isGray() {
    return this.color === "gray";
  }

  isRed() {
    return this.color === "red";
  }

}
