import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NetworkNodeRow } from '@api/common/network';
import { IndicatorComponent } from '@app/components/shared/indicator';
import { NodeConnectionIndicatorDialogComponent } from './node-connection-indicator-dialog.component';

@Component({
  selector: 'kpn-node-connection-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator
      letter="C"
      i18n-letter="@@node-connection-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()"
    />
  `,
  standalone: true,
  imports: [IndicatorComponent],
})
export class NodeConnectionIndicatorComponent implements OnInit {
  node = input.required<NetworkNodeRow>();

  private readonly dialog = inject(MatDialog);
  color: string;

  ngOnInit(): void {
    this.color = this.node().detail.connection ? 'blue' : 'gray';
  }

  onOpenDialog() {
    this.dialog.open(NodeConnectionIndicatorDialogComponent, {
      data: this.color,
      autoFocus: false,
      maxWidth: 600,
    });
  }
}
