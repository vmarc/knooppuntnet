import {Component} from "@angular/core";
import {MatDialog} from "@angular/material";
import {ConnectionIndicatorDialogComponent} from "./connection-indicator-dialog.component";

@Component({
  selector: "kpn-connection-indicator",
  template: `
    <kpn-indicator letter="C" [color]="color()" (openDialog)="onOpenDialog()"></kpn-indicator>
  `
})
export class ConnectionIndicatorComponent {

  constructor(private dialog: MatDialog) {
  }

  onOpenDialog() {
    this.dialog.open(ConnectionIndicatorDialogComponent, {data: this.color()});
  }

  color() {
    return "blue";
  }

}
