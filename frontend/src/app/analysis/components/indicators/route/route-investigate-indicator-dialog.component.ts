import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { IndicatorDialogComponent } from '@app/components/shared/indicator';

@Component({
  selector: 'kpn-route-investigate-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog
      letter="F"
      i18n-letter="@@route-investigate-indicator.letter"
      [color]="color"
    >
      <span dialog-title *ngIf="isGreen()" i18n="@@route-investigate-indicator.green.title">
        OK - No facts
      </span>
      <div dialog-body *ngIf="isGreen()" i18n="@@route-investigate-indicator.green.text">
        No issues found during route analysis.
      </div>

      <span dialog-title *ngIf="isRed()" i18n="@@route-investigate-indicator.red.title">
        Not OK - Investigate facts
      </span>
      <div dialog-body *ngIf="isRed()" i18n="@@route-investigate-indicator.red.text">
        Something is wrong with this route.
      </div>
    </kpn-indicator-dialog>
  `,
  standalone: true,
  imports: [IndicatorDialogComponent, NgIf],
})
export class RouteInvestigateIndicatorDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public color: string) {}

  isGreen() {
    return this.color === 'green';
  }

  isRed() {
    return this.color === 'red';
  }
}
