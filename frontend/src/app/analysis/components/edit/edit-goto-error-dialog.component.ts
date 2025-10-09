import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { DialogComponent } from '@app/shared/components/dialog/dialog.component';

@Component({
  selector: 'ui-edit-goto-error-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-dialog>
      <div dialog-title class="dialog" i18n="@@edit-goto-error-dialog.title">JOSM</div>
      <div>
        <p i18n="@@edit-goto-error-dialog.error">
          Sorry, could not pan/zoom to current location in JOSM.
        </p>
        <ul>
          <li i18n="@@edit-goto-error-dialog.editor-not-started">JOSM not started?</li>
          <li i18n="@@edit-goto-error-dialog.remote-control-not-enabled">
            JOSM remote control not enabled?
          </li>
        </ul>
      </div>
    </ui-dialog>
  `,
  imports: [DialogComponent],
})
export class EditGotoErrorDialogComponent {}
