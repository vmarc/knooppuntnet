import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'kpn-icon-investigate',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <mat-icon svgIcon="investigate" /> `,
  styles: `
    :host {
      width: 18px;
      height: 18px;
    }

    mat-icon {
      width: 20px;
      height: 20px;
    }
  `,
  standalone: true,
  imports: [MatIconModule],
})
export class IconInvestigateComponent {}
