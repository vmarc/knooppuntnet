import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatDialogContent } from '@angular/material/dialog';
import { DialogComponent } from '../dialog/dialog.component';

@Component({
  selector: 'kpn-timeout',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-content>
        <p i18n="@@timeout.message-1">Sorry.</p>
        <p i18n="@@timeout.message-2">No response from editor.</p>
        <p i18n="@@timeout.message-3">
          Has the editor (JOSM) been started? Has remote control been enabled in the editor?
        </p>
      </div>
    </kpn-dialog>
  `,
  standalone: true,
  imports: [DialogComponent, MatDialogContent],
})
export class TimeoutComponent {}
