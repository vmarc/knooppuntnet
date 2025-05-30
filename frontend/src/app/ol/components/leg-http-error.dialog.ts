import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { DialogComponent } from '@app/shared/components/dialog/dialog.component';

@Component({
  selector: 'ui-leg-http-error-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-dialog>
      <div mat-dialog-title i18n="@@leg-http-error-dialog.title">Network error</div>
      <div mat-dialog-content i18n="@@leg-http-error-dialog.message">
        The planner could not contact the server, please try again later.
      </div>
    </ui-dialog>
  `,
  imports: [DialogComponent, MatDialogModule],
})
export class LegHttpErrorDialogComponent {}
