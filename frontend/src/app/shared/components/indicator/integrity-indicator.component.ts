import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { IndicatorComponent } from './indicator.component';
import { IntegrityIndicatorData } from './integrity-indicator-data';
import { IntegrityIndicatorDialogComponent } from './integrity-indicator-dialog.component';

@Component({
  selector: 'ui-integrity-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-indicator
      letter="E"
      i18n-letter="@@integrity-indicator.letter"
      [color]="data().color()"
      (openDialog)="onOpenDialog()"
    />
  `,
  imports: [IndicatorComponent],
})
export class IntegrityIndicatorComponent {
  readonly data = input.required<IntegrityIndicatorData>();

  private readonly dialog = inject(MatDialog);

  onOpenDialog() {
    this.dialog.open(IntegrityIndicatorDialogComponent, {
      data: this.data(),
      autoFocus: false,
      maxWidth: 600,
    });
  }
}
