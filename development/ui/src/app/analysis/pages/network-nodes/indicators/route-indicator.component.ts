import {Component} from '@angular/core';
import {MatDialog} from "@angular/material";
import {RouteIndicatorDialogComponent} from "./route-indicator-dialog.component";

@Component({
  selector: 'kpn-route-indicator',
  template: `
    <kpn-indicator letter="R" [color]="color()" (openDialog)="onOpenDialog()"></kpn-indicator>
  `
})
export class RouteIndicatorComponent {

  constructor(private dialog: MatDialog) {
  }

  onOpenDialog() {
    this.dialog.open(RouteIndicatorDialogComponent, {data: this.color()});
  }

  color() {
    return "green";
  }

}
