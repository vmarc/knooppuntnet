import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NetworkNodeDetail } from '@api/common/network/network-node-detail';
import { NetworkType } from '@api/custom/network-type';
import { IntegrityIndicatorData } from './integrity-indicator-data';
import { IntegrityIndicatorDialogComponent } from './integrity-indicator-dialog.component';

@Component({
  selector: 'kpn-integrity-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator
      letter="E"
      i18n-letter="@@integrity-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()"
    >
    </kpn-indicator>
  `,
})
export class IntegrityIndicatorComponent implements OnInit {
  @Input() networkType: NetworkType;
  @Input() node: NetworkNodeDetail;
  color: string;

  constructor(private dialog: MatDialog) {}

  ngOnInit(): void {
    this.color = this.determineColor();
  }

  onOpenDialog() {
    const indicatorData = new IntegrityIndicatorData(
      this.color,
      this.networkType,
      this.node.routeReferences.length,
      this.node.expectedRouteCount
    );
    this.dialog.open(IntegrityIndicatorDialogComponent, {
      data: indicatorData,
      maxWidth: 600,
    });
  }

  private determineColor() {
    let color;
    if (this.node.expectedRouteCount !== '-') {
      if (+this.node.expectedRouteCount !== this.node.routeReferences.length) {
        color = 'red';
      } else {
        color = 'green';
      }
    } else {
      color = 'gray';
    }
    return color;
  }
}
