import { Component, Input } from '@angular/core';
import { AbstractControl } from '@angular/forms';
import { FormGroupDirective } from '@angular/forms';
import { Util } from './util';

@Component({
  selector: 'app-field-errors',
  template: `
    <div *ngIf="show()" class="errors">
      <div *ngFor="let error of util.toMessages(control.errors)">
        {{ error }}
      </div>
    </div>
  `,
  styles: [
    `
      .errors {
        color: red;
      }
    `,
  ],
})
export class FieldErrorsComponent {
  @Input() control: AbstractControl;

  readonly util = Util;

  constructor(private formGroupDirective: FormGroupDirective) {}

  show(): boolean {
    return (
      this.control &&
      this.control.invalid &&
      (this.control.dirty ||
        this.control.touched ||
        this.formGroupDirective.submitted)
    );
  }
}
