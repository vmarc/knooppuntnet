import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NetworkNodeRow } from '@api/common/network/network-node-row';
import { IndicatorComponent } from '@app/shared/components/indicator/indicator.component';
import { NodeConnectionIndicatorDialogComponent } from './node-connection-indicator-dialog.component';

@Component({
  selector: 'ui-node-connection-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-indicator
      letter="C"
      i18n-letter="@@node-connection-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()"
    />
  `,
  imports: [IndicatorComponent],
})
export class NodeConnectionIndicatorComponent implements OnInit {
  readonly node = input.required<NetworkNodeRow>();

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
