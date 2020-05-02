import {ChangeDetectionStrategy} from "@angular/core";
import {OnInit} from "@angular/core";
import {Component, Input} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {NetworkInfoNode} from "../../../../kpn/api/common/network/network-info-node";
import {NodeConnectionIndicatorDialogComponent} from "./node-connection-indicator-dialog.component";

@Component({
  selector: "kpn-node-connection-indicator",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator
      letter="C"
      i18n-letter="@@node-connection-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()">
    </kpn-indicator>
  `
})
export class NodeConnectionIndicatorComponent implements OnInit {

  @Input() node: NetworkInfoNode;
  color: string;

  constructor(private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.color = this.node.connection ? "blue" : "gray";
  }

  onOpenDialog() {
    this.dialog.open(NodeConnectionIndicatorDialogComponent, {data: this.color});
  }

}
