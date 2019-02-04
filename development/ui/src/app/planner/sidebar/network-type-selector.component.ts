import {Component, Input} from '@angular/core';
import {NetworkType} from "../../kpn/shared/network-type";

@Component({
  selector: 'kpn-network-type-selector',
  template: `
    <div>
      <mat-button-toggle-group [value]="networkType.name">
        <mat-button-toggle value="rcn">
          <mat-icon svgIcon="rcn"></mat-icon>
        </mat-button-toggle>
        <mat-button-toggle value="rwn">
          <mat-icon svgIcon="rwn"></mat-icon>
        </mat-button-toggle>
        <mat-button-toggle value="rhn">
          <mat-icon svgIcon="rhn"></mat-icon>
        </mat-button-toggle>
        <mat-button-toggle value="rmn">
          <mat-icon svgIcon="rmn"></mat-icon>
        </mat-button-toggle>
        <mat-button-toggle value="rpn">
          <mat-icon svgIcon="rpn"></mat-icon>
        </mat-button-toggle>
        <mat-button-toggle value="rin">
          <mat-icon svgIcon="rin"></mat-icon>
        </mat-button-toggle>
      </mat-button-toggle-group>
    </div>
  `
})
export class NetworkTypeSelectorComponent {
  @Input() networkType: NetworkType = new NetworkType("rcn"); // TODO cleanup
}
