import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'kpn-leg-http-error-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title i18n="@@leg-http-error-dialog.title">
        Network error
      </div>
      <div mat-dialog-content i18n="@@leg-http-error-dialog.message">
        The planner could not contact the server, please try again later.
      </div>
    </kpn-dialog>
  `,
})
export class LegHttpErrorDialogComponent {}
