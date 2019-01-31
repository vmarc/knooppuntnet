import {Component, Input} from '@angular/core';
import {NetworkType} from "../../kpn/shared/network-type";

@Component({
  selector: 'kpn-network-type-selector',
  template: `
    <div>
      <mat-button-toggle-group [value]="networkType.name">
        <mat-button-toggle value="rcn">
          <kpn-icon-network-type-bicycle></kpn-icon-network-type-bicycle>
        </mat-button-toggle>
        <mat-button-toggle value="rwn">
          <kpn-icon-network-type-hiking></kpn-icon-network-type-hiking>
        </mat-button-toggle>
        <mat-button-toggle value="rhn">
          <kpn-icon-network-type-horse></kpn-icon-network-type-horse>
        </mat-button-toggle>
        <mat-button-toggle value="rmn">
          <kpn-icon-network-type-motorboat></kpn-icon-network-type-motorboat>
        </mat-button-toggle>
        <mat-button-toggle value="rpn">
          <kpn-icon-network-type-canoe></kpn-icon-network-type-canoe>
        </mat-button-toggle>
        <mat-button-toggle value="rin">
          <kpn-icon-network-type-inline-skates></kpn-icon-network-type-inline-skates>
        </mat-button-toggle>
      </mat-button-toggle-group>
    </div>
  `
})
export class NetworkTypeSelectorComponent {
  @Input() networkType: NetworkType = new NetworkType("rcn"); // TODO cleanup
}
