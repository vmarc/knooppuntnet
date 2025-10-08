import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { DialogComponent } from '@app/shared/components/dialog/dialog.component';

@Component({
  selector: 'ui-leg-not-found-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-dialog>
      <div dialog-title i18n="@@leg-not-found-dialog.title">No path</div>
      <div i18n="@@leg-not-found-dialog.message">
        The planner did not find a path to the selected destination.
      </div>
    </ui-dialog>
  `,
  imports: [DialogComponent],
})
export class LegNotFoundDialogComponent {}
