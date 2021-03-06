import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-icon-investigate',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <mat-icon svgIcon="investigate"></mat-icon> `,
  styles: [
    `
      mat-icon {
        width: 20px;
        height: 20px;
      }
    `,
  ],
})
export class IconInvestigateComponent {}
