import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'kpn-icon-happy',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <mat-icon svgIcon="happy" /> `,
  styles: `
    :host {
      width: 25px;
      height: 25px;
    }

    mat-icon {
      width: 25px;
      height: 25px;
    }
  `,
  standalone: true,
  imports: [MatIconModule],
})
export class IconHappyComponent {}
