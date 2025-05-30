import { effect } from '@angular/core';
import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDialogRef } from '@angular/material/dialog';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { Translations } from '@app/shared/i18n/translations';
import { Subscriptions } from '@app/util/subscriptions';
import { EditParameters } from './edit-parameters';
import { EditService } from './edit.service';

@Component({
  selector: 'ui-edit-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div mat-dialog-title class="dialog" i18n="@@edit-dialog.title">Load in editor</div>

    <div mat-dialog-content>
      @if (editService.showProgress()) {
        <p>
          <mat-progress-bar [value]="editService.progress()" />
        </p>
      }
      @if (editService.error()) {
        <p i18n="@@edit-dialog.error">Sorry, could not load elements in editor.</p>
      }
      @if (editService.errorName(); as errorName) {
        <p>
          {{ errorName }}
        </p>
      }
      @if (editService.errorCouldNotConnect()) {
        <ul>
          <li i18n="@@edit-dialog.editor-not-started">Editor not started?</li>
          <li i18n="@@edit-dialog.remote-control-not-enabled">
            Editor remote control not enabled?
          </li>
        </ul>
      }
      @if (editService.errorMessage(); as errorMessage) {
        <p>
          {{ errorMessage }}
        </p>
      }
      @if (editService.timeout()) {
        <p class="timeout" i18n="@@edit-dialog.timeout">
          Timeout: editor not started, or editor remote control not enabled?
        </p>
      }
    </div>
    <div mat-dialog-actions>
      @if (editService.showProgress()) {
        <p>
          <button mat-raised-button (click)="cancel()">{{ cancelButtonText }}</button>
        </p>
      }
      @if (editService.error()) {
        <p>
          <button mat-raised-button (click)="close()" i18n="@@edit-dialog.close">Close</button>
        </p>
      }
    </div>
  `,
  styles: `
    .dialog {
      min-width: 20em;
    }

    .timeout {
      color: red;
    }
  `,
  providers: [EditService],
  imports: [MatButtonModule, MatDialogModule, MatProgressBarModule],
})
export class EditDialogComponent implements OnInit, OnDestroy {
  protected readonly parameters: EditParameters = inject(MAT_DIALOG_DATA);
  protected readonly editService = inject(EditService);
  private readonly dialogRef = inject(MatDialogRef<EditDialogComponent>);
  protected readonly cancelButtonText = Translations.get('action.cancel');
  private readonly subscriptions = new Subscriptions();

  constructor() {
    effect(() => {
      if (this.editService.ready()) {
        this.dialogRef.close();
      }
    });
  }

  ngOnInit(): void {
    this.editService.edit(this.parameters);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  cancel(): void {
    this.editService.cancel();
    this.dialogRef.close();
  }

  close(): void {
    this.dialogRef.close();
  }
}
