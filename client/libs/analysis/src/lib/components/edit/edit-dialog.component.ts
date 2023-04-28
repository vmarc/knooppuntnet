import { NgIf } from '@angular/common';
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
    <div mat-dialog-title class="dialog" i18n="@@edit-dialog.title">
      Load in editor
    </div>

    <div mat-dialog-content>
      <p *ngIf="editService.showProgress$ | async">
        <mat-progress-bar
          [value]="editService.progress$ | async"
        ></mat-progress-bar>
      </p>
      <p *ngIf="editService.error$ | async" i18n="@@edit-dialog.error">
        Sorry, could not load elements in editor.
      </p>
      <p *ngIf="editService.errorName$ | async as errorName">
        {{ errorName }}
      </p>
      <ul *ngIf="editService.errorCouldNotConnect$ | async">
        <li i18n="@@edit-dialog.editor-not-started">Editor not started?</li>
        <li i18n="@@edit-dialog.remote-control-not-enabled">
          Editor remote control not enabled?
        </li>
      </ul>
      <p *ngIf="editService.errorMessage$ | async as errorMessage">
        {{ errorMessage }}
      </p>
      <p
        *ngIf="editService.timeout$ | async"
        class="timeout"
        i18n="@@edit-dialog.timeout"
      >
        Timeout: editor not started, or editor remote control not enabled?
      </p>
    </div>
    <div mat-dialog-actions>
      <p *ngIf="editService.showProgress$ | async">
        <button mat-raised-button (click)="cancel()" i18n="@@action.cancel">
          Cancel
        </button>
      </p>
      <p *ngIf="editService.error$ | async">
        <button mat-raised-button (click)="close()" i18n="@@edit-dialog.close">
          Close
        </button>
      </p>
    </div>
  `,
  styles: [
    `
      .dialog {
        min-width: 20em;
      }

      .timeout {
        color: red;
      }
    `,
  ],
  providers: [EditService],
  standalone: true,
  imports: [
    MatDialogModule,
    NgIf,
    MatProgressBarModule,
    MatButtonModule,
    AsyncPipe,
  ],
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
