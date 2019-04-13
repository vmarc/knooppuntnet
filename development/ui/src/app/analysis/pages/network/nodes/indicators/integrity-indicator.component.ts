import {Component} from "@angular/core";
import {MatDialog} from "@angular/material";
import {IntegrityIndicatorDialogComponent} from "./integrity-indicator-dialog.component";

@Component({
  selector: "kpn-integrity-indicator",
  template: `
    <kpn-indicator letter="E" [color]="color()" (openDialog)="onOpenDialog()"></kpn-indicator>
  `
})
export class IntegrityIndicatorComponent {

  constructor(private dialog: MatDialog) {
  }

  onOpenDialog() {
    this.dialog.open(IntegrityIndicatorDialogComponent, {data: this.color()});
  }

  color() {
    return "gray";
  }

}
