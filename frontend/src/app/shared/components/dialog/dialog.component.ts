import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button nz-button class="close-button" mat-dialog-close>
      <nz-icon nzType="close" />
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
  imports: [MatDialogModule, NzIconDirective, NzButtonComponent],
})
export class DialogComponent {}
