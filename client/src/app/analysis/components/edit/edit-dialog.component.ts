import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Subscriptions } from '../../../util/Subscriptions';
import { EditParameters } from './edit-parameters';
import { EditService } from './edit.service';

@Component({
  selector: 'kpn-edit-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="dialog">
      <div mat-dialog-title i18n="@@edit-dialog.title">Load in editor</div>

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
          <button
            mat-raised-button
            (click)="close()"
            i18n="@@edit-dialog.close"
          >
            Close
          </button>
        </p>
      </div>
    </div>
  `,
  styles: [
    `
      .dialog {
        min-width: 14em;
      }

      .timeout {
        color: red;
      }
    `,
  ],
  providers: [EditService],
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
