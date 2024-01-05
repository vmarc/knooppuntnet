import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NetworkType } from '@api/custom';
import { IndicatorDialogComponent } from '@app/components/shared/indicator';
import { RouteAccessibleData } from './route-accessible-data';

@Component({
  selector: 'kpn-route-acccessible-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator-dialog
      letter="A"
      i18n-letter="@@route-accessible-indicator.letter"
      [color]="data.color"
    >
      @if (data.color === 'gray') {
        <span dialog-title i18n="@@route-accessible-indicator.gray.title">
          OK - Accessibility unknown
        </span>
      }
      @if (data.color === 'gray') {
        <div dialog-body i18n="@@route-accessible-indicator.gray.text">
          Accessibility information is unknown for this type of route.
        </div>
      }

      @if (data.color === 'green') {
        <span dialog-title i18n="@@route-accessible-indicator.green.title"> OK - Accessible </span>
      }
      @if (data.color === 'green') {
        <div dialog-body>
          @switch (data.networkType) {
            @case (NetworkType.cycling) {
              <ng-container i18n="@@route-accessible-indicator.green.text.cycling">
                This route is completely accessible for bicycle.
              </ng-container>
            }
            @case (NetworkType.hiking) {
              <ng-container i18n="@@route-accessible-indicator.green.text.hiking">
                This route is completely accessible for hiking.
              </ng-container>
            }
            @case (NetworkType.motorboat) {
              <ng-container i18n="@@route-accessible-indicator.green.text.motorboat">
                This route is completely accessible for motorboat.
              </ng-container>
            }
            @case (NetworkType.canoe) {
              <ng-container i18n="@@route-accessible-indicator.green.text.canoe">
                This route is completely accessible for canoe.
              </ng-container>
            }
          }
        </div>
      }

      @if (data.color === 'red') {
        <span dialog-title i18n="@@route-accessible-indicator.red.title">
          Not OK - Not Accessible
        </span>
      }
      @if (data.color === 'red') {
        <div dialog-body>
          @switch (data.networkType) {
            @case (NetworkType.cycling) {
              <ng-container i18n="@@route-accessible-indicator.red.text.cycling">
                This route is not completely accessible for bicycle.
              </ng-container>
            }
            @case (NetworkType.hiking) {
              <ng-container i18n="@@route-accessible-indicator.red.text.hiking">
                This route is not completely accessible for hiking.
              </ng-container>
            }
            @case (NetworkType.motorboat) {
              <ng-container i18n="@@route-accessible-indicator.red.text.motorboat">
                This route is not completely accessible for motorboat.
              </ng-container>
            }
            @case (NetworkType.canoe) {
              <ng-container i18n="@@route-accessible-indicator.red.text.canoe">
                This route is not completely accessible for canoe.
              </ng-container>
            }
          }
        </div>
      }
    </kpn-indicator-dialog>
  `,
  standalone: true,
  imports: [IndicatorDialogComponent],
})
export class RouteAccessibleIndicatorDialogComponent {
  protected readonly data: RouteAccessibleData = inject(MAT_DIALOG_DATA);
  protected readonly NetworkType = NetworkType;
}
