import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { DialogComponent } from '@app/components/shared/dialog';

@Component({
  selector: 'kpn-leg-not-found-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title i18n="@@leg-not-found-dialog.title">No path</div>
      <div mat-dialog-content i18n="@@leg-not-found-dialog.message">
        The planner did not find a path to the selected destination.
      </div>
    </kpn-dialog>
  `,
  standalone: true,
  imports: [DialogComponent, MatDialogModule],
})
export class LegNotFoundDialogComponent {}
