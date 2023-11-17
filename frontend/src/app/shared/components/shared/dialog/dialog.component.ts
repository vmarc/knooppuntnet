import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'kpn-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button mat-icon-button class="close-button" mat-dialog-close>
      <mat-icon svgIcon="remove" />
    </button>
    <ng-content />
  `,
  styles: `
    .close-button {
      background-color: white;
      float: right;
      z-index: 100;
    }
  `,
  standalone: true,
  imports: [MatButtonModule, MatDialogModule, MatIconModule],
})
export class DialogComponent {}
