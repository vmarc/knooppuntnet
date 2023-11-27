import { ChangeDetectionStrategy, Component } from '@angular/core';
import { DialogComponent } from '../dialog/dialog.component';

@Component({
  selector: 'kpn-timeout',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <p i18n="@@timeout.message-1">Sorry.</p>
      <p i18n="@@timeout.message-2">No response from editor.</p>
      <p i18n="@@timeout.message-3">
        Has the editor (JOSM) been started? Has remote control been enabled in the editor?
      </p>
    </kpn-dialog>
  `,
  standalone: true,
  imports: [DialogComponent],
})
export class TimeoutComponent {}
