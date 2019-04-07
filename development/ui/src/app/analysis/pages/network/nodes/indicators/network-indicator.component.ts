import {Component} from '@angular/core';
import {MatDialog} from "@angular/material";
import {NetworkIndicatorDialogComponent} from "./network-indicator-dialog.component";

@Component({
  selector: 'kpn-network-indicator',
  template: `
    <kpn-indicator letter="N" [color]="color()" (openDialog)="onOpenDialog()"></kpn-indicator>
  `
})
export class NetworkIndicatorComponent {

  constructor(private dialog: MatDialog) {
  }

  onOpenDialog() {
    this.dialog.open(NetworkIndicatorDialogComponent, {data: this.color()});
  }

  color() {
    return "gray";
  }

}
