import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { DialogComponent } from './dialog.component';
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
  standalone: true,
  imports: [DialogComponent, MatDialogModule],
})
export class WarningDialogComponent {
  protected readonly data: WarningDialogData = inject(MAT_DIALOG_DATA);
}
