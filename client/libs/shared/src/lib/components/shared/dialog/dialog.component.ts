import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'kpn-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button mat-icon-button class="close-button" mat-dialog-close>
      <mat-icon svgIcon="remove" />
    </button>
    <ng-content />
  `,
  styles: [
    `
      .close-button {
        background-color: white;
        float: right;
        z-index: 100;
      }
    `,
  ],
})
export class DialogComponent {}
