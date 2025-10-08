import { ChangeDetectionStrategy, Component } from '@angular/core';
import { DialogComponent } from '@app/shared/components/dialog/dialog.component';

@Component({
  selector: 'ui-timeout',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-dialog>
      <div dialog-title i18n="@@timeout.message-1">Sorry</div>
      <div>
        <p i18n="@@timeout.message-2">No response from editor.</p>
        <p i18n="@@timeout.message-3">
          Has the editor (JOSM) been started? Has remote control been enabled in the editor?
        </p>
      </div>
    </ui-dialog>
  `,
  imports: [DialogComponent],
})
export class TimeoutComponent {}
