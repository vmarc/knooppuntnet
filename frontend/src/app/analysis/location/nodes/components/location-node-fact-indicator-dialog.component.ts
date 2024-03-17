import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { IndicatorDialogComponent } from '@app/components/shared/indicator';

@Component({
  selector: 'kpn-location-node-fact-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog
      letter="F"
      i18n-letter="@@location-node-fact-indicator.letter"
      [color]="color"
    >
      @if (color === 'green') {
        <span dialog-title i18n="@@location-node-fact-indicator.green.title">OK - No facts</span>
      }
      @if (color === 'green') {
        <div dialog-body i18n="@@location-node-fact-indicator.green.text">
          No particular facts were found during the analysis of this node.
        </div>
      }
      @if (color === 'red') {
        <span dialog-title i18n="@@location-node-fact-indicator.red.title"> Facts </span>
      }
      @if (color === 'red') {
        <div dialog-body i18n="@@location-node-fact-indicator.red.text">
          One or more facts are generated as the result of the node analysis.
        </div>
      }
    </kpn-indicator-dialog>
  `,
  standalone: true,
  imports: [IndicatorDialogComponent],
})
export class LocationNodeFactIndicatorDialogComponent {
  protected readonly color: string = inject(MAT_DIALOG_DATA);
}
