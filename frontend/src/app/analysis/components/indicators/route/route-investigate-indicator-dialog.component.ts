import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
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
      @switch (color) {
        @case ('green') {
          <span dialog-title i18n="@@route-investigate-indicator.green.title"> OK - No facts </span>
          <div dialog-body i18n="@@route-investigate-indicator.green.text">
            No issues found during route analysis.
          </div>
        }
        @case ('red') {
          <span dialog-title i18n="@@route-investigate-indicator.red.title">
            Not OK - Investigate facts
          </span>
          <div dialog-body i18n="@@route-investigate-indicator.red.text">
            Something is wrong with this route.
          </div>
        }
      }
    </kpn-indicator-dialog>
  `,
  standalone: true,
  imports: [IndicatorDialogComponent],
})
export class RouteInvestigateIndicatorDialogComponent {
  protected readonly color: string = inject(MAT_DIALOG_DATA);
}
