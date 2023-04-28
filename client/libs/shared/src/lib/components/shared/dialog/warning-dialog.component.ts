import { Inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { WarningDialogData } from './warning-dialog-data';

@Component({
  selector: 'kpn-warning-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title>
        {{ data.title }}
      </div>
      <div mat-dialog-content>
        {{ data.message }}
      </div>
    </kpn-dialog>
  `,
})
export class WarningDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: WarningDialogData) {}
}
