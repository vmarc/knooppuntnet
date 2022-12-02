import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NetworkNodeRow } from '@api/common/network/network-node-row';
import { NetworkIndicatorDialogComponent } from './network-indicator-dialog.component';

@Component({
  selector: 'kpn-network-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator
      letter="N"
      i18n-letter="@@network-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()"
    >
    </kpn-indicator>
  `,
})
export class NetworkIndicatorComponent implements OnInit {
  @Input() node: NetworkNodeRow;
  color: string;

  constructor(private dialog: MatDialog) {}

  ngOnInit(): void {
    this.color = this.determineColor();
  }

  onOpenDialog() {
    this.dialog.open(NetworkIndicatorDialogComponent, {
      data: this.color,
      autoFocus: false,
      maxWidth: 600,
    });
  }

  private determineColor() {
    let color;
    if (this.node.detail.definedInRelation) {
      if (this.node.detail.connection && !this.node.detail.roleConnection) {
        color = 'orange';
      } else {
        color = 'green';
      }
    } else {
      if (this.node.detail.connection) {
        color = 'gray';
      } else {
        color = 'red';
      }
    }
    return color;
  }
}
