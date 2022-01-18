import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'kpn-leg-not-found-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title i18n="@@location-expand-all-dialog.title">
        Expand all
      </div>
      <div mat-dialog-content>
        <p i18n="@@location-expand-all-dialog.message.1">
          Expanding all locations in the tree is a heavy operation for your
          browser, and it will take some time.
        </p>
        <p i18n="@@location-expand-all-dialog.message.2">
          Are you sure you want to expand all?
        </p>
      </div>
      <div mat-dialog-actions>
        <button mat-stroked-button (click)="onNoClick()" i18n="@@no">No</button>
        <button
          mat-raised-button
          color="primary"
          cdkFocusInitial
          (click)="onYesClick()"
          i18n="@@yes"
        >
          Yes
        </button>
      </div>
    </kpn-dialog>
  `,
})
export class LocationExpandAllDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<LocationExpandAllDialogComponent>
  ) {}

  onYesClick(): void {
    this.dialogRef.close(true);
  }

  onNoClick(): void {
    this.dialogRef.close(false);
  }
}
