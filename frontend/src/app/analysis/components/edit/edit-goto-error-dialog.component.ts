import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDialogRef } from '@angular/material/dialog';
import { NzButtonComponent } from 'ng-zorro-antd/button';

@Component({
  selector: 'ui-edit-goto-error-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div mat-dialog-title class="dialog" i18n="@@edit-goto-error-dialog.title">JOSM</div>
    <div mat-dialog-content>
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
    <div mat-dialog-actions>
      <p>
        <button nz-button nzType="link" (click)="close()" i18n="@@edit-goto-error-dialog.close">
          Close
        </button>
      </p>
    </div>
  `,
  styles: `
    .dialog {
      min-width: 20em;
    }
  `,
  imports: [MatDialogModule, NzButtonComponent],
})
export class EditGotoErrorDialogComponent {
  private readonly dialogRef = inject(MatDialogRef<EditGotoErrorDialogComponent>);

  close(): void {
    this.dialogRef.close();
  }
}
