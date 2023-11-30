import { AsyncPipe } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDialogRef } from '@angular/material/dialog';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { Subscriptions } from '@app/util';
import { EditParameters } from './edit-parameters';
import { EditService } from './edit.service';

@Component({
  selector: 'kpn-edit-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div mat-dialog-title class="dialog" i18n="@@edit-dialog.title">Load in editor</div>

    <div mat-dialog-content>
      @if (editService.showProgress$ | async) {
        <p>
          <mat-progress-bar [value]="editService.progress$ | async"></mat-progress-bar>
        </p>
      }
      @if (editService.error$ | async) {
        <p i18n="@@edit-dialog.error">Sorry, could not load elements in editor.</p>
      }
      @if (editService.errorName$ | async; as errorName) {
        <p>
          {{ errorName }}
        </p>
      }
      @if (editService.errorCouldNotConnect$ | async) {
        <ul>
          <li i18n="@@edit-dialog.editor-not-started">Editor not started?</li>
          <li i18n="@@edit-dialog.remote-control-not-enabled">
            Editor remote control not enabled?
          </li>
        </ul>
      }
      @if (editService.errorMessage$ | async; as errorMessage) {
        <p>
          {{ errorMessage }}
        </p>
      }
      @if (editService.timeout$ | async) {
        <p class="timeout" i18n="@@edit-dialog.timeout">
          Timeout: editor not started, or editor remote control not enabled?
        </p>
      }
    </div>
    <div mat-dialog-actions>
      @if (editService.showProgress$ | async) {
        <p>
          <button mat-raised-button (click)="cancel()" i18n="@@action.cancel">Cancel</button>
        </p>
      }
      @if (editService.error$ | async) {
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
  standalone: true,
  imports: [AsyncPipe, MatButtonModule, MatDialogModule, MatProgressBarModule],
})
export class EditDialogComponent implements OnInit, OnDestroy {
  private readonly subscriptions = new Subscriptions();

  constructor(
    private dialogRef: MatDialogRef<EditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public parameters: EditParameters,
    public editService: EditService
  ) {}

  ngOnInit(): void {
    this.closeDialogUponReady();
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

  private closeDialogUponReady(): void {
    this.subscriptions.add(
      this.editService.ready$.subscribe((ready) => {
        if (ready) {
          this.dialogRef.close();
        }
      })
    );
  }
}
