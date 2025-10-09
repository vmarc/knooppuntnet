import { effect } from '@angular/core';
import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { EditStepBuilder } from '@app/analysis/components/edit/edit-step-builder';
import { EditService } from '@app/analysis/components/edit/edit.service';
import { Translations } from '@app/shared/i18n/translations';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzModalRef } from 'ng-zorro-antd/modal';
import { NZ_MODAL_DATA } from 'ng-zorro-antd/modal';
import { NzProgressComponent } from 'ng-zorro-antd/progress';
import { EditParameters } from './edit-parameters';

@Component({
  selector: 'ui-edit-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/cyclomatic-complexity -->
    <div>
      @if (editService.showProgress()) {
        <p>
          <nz-progress [nzPercent]="editService.progress()" [nzShowInfo]="false" />
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
    <div>
      @if (editService.showProgress()) {
        <p>
          <button nz-button (click)="cancel()">{{ cancelButtonText }}</button>
        </p>
      }
      @if (editService.error()) {
        <p>
          <button nz-button (click)="close()" i18n="@@edit-dialog.close">Close</button>
        </p>
      }
    </div>
  `,
  providers: [EditService, EditStepBuilder],
  styles: `
    .timeout {
      color: red;
    }
  `,
  imports: [NzButtonComponent, NzProgressComponent],
})
export class EditDialogComponent implements OnInit {
  protected readonly editService = inject(EditService);
  private readonly parameters: EditParameters = inject(NZ_MODAL_DATA);
  private readonly modalRef = inject(NzModalRef);

  protected readonly cancelButtonText = Translations.get('action.cancel');

  constructor() {
    effect(() => {
      if (this.editService.ready()) {
        this.modalRef.close();
      }
    });
  }

  ngOnInit(): void {
    this.editService.edit(this.parameters);
  }

  cancel(): void {
    this.editService.cancel();
    this.modalRef.close();
  }

  close(): void {
    this.modalRef.close();
  }
}
