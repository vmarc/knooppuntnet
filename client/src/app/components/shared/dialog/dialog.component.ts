import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'kpn-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button mat-icon-button class="close-button" mat-dialog-close>
      <mat-icon svgIcon="remove"></mat-icon>
    </button>
    <ng-content></ng-content>
  `,
  styles: [
    `
      .close-button {
        background-color: white;
        float: right;
        top: -24px;
        right: -24px;
      }

      .mat-icon-button ::ng-deep .mat-button-focus-overlay {
        display: none;
      }
    `,
  ],
})
export class DialogComponent {}
