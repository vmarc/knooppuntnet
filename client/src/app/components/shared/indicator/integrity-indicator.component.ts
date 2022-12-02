import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { IntegrityIndicatorData } from './integrity-indicator-data';
import { IntegrityIndicatorDialogComponent } from './integrity-indicator-dialog.component';

@Component({
  selector: 'kpn-integrity-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator
      letter="E"
      i18n-letter="@@integrity-indicator.letter"
      [color]="data.color()"
      (openDialog)="onOpenDialog()"
    >
    </kpn-indicator>
  `,
})
export class IntegrityIndicatorComponent {
  @Input() data: IntegrityIndicatorData;

  constructor(private dialog: MatDialog) {}

  onOpenDialog() {
    this.dialog.open(IntegrityIndicatorDialogComponent, {
      data: this.data,
      autoFocus: false,
      maxWidth: 600,
    });
  }
}
