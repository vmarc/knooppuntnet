import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NetworkNodeDetail } from '@api/common/network/network-node-detail';
import {ProposedIndicatorDialogComponent} from "@app/analysis/network/nodes/indicators/proposed-indicator-dialog.component";

@Component({
  selector: 'kpn-proposed-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator
      letter="P"
      i18n-letter="@@proposed-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()"
    >
    </kpn-indicator>
  `,
})
export class ProposedIndicatorComponent implements OnInit {
  @Input() node: NetworkNodeDetail;
  color: string;

  constructor(private dialog: MatDialog) {}

  ngOnInit(): void {
    this.color = this.node.proposed ? 'blue' : 'gray';
  }

  onOpenDialog() {
    this.dialog.open(ProposedIndicatorDialogComponent, {
      data: this.color,
      maxWidth: 600,
    });
  }
}
