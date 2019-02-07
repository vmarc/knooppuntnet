import {Component} from '@angular/core';
import {MatDialog} from "@angular/material";
import {RoleConnectionIndicatorDialogComponent} from "./role-connection-indicator-dialog.component";

@Component({
  selector: 'kpn-role-connection-indicator',
  template: `
    <kpn-indicator letter="C" [color]="color()" (openDialog)="onOpenDialog()"></kpn-indicator>
  `,
  styles: []
})
export class RoleConnectionIndicatorComponent {

  constructor(private dialog: MatDialog) {
  }

  onOpenDialog() {
    this.dialog.open(RoleConnectionIndicatorDialogComponent, {data: this.color()});
  }

  color() {
    return "orange";
  }

}
