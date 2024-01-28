import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { DialogComponent } from '../dialog/dialog.component';
import { IndicatorIconComponent } from './indicator-icon.component';

@Component({
  selector: 'kpn-indicator-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title class="title">
        <kpn-indicator-icon [letter]="letter()" [color]="color()" />
        <div class="title-text">
          <ng-content select="[dialog-title]" />
        </div>
      </div>
      <div mat-dialog-content>
        <ng-content select="[dialog-body]" />
      </div>
    </kpn-dialog>
  `,
  styles: `
    .title {
      display: flex;
      align-items: center;
      padding-top: 0.5em;
    }

    .title-text {
      display: inline-block;
      padding-top: 5px;
      padding-left: 20px;
    }
  `,
  standalone: true,
  imports: [DialogComponent, MatDialogModule, IndicatorIconComponent],
})
export class IndicatorDialogComponent {
  letter = input.required<string>();
  color = input<string | undefined>();
}
