import { Component } from '@angular/core';
import { ChangeDetectorRef } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { Util } from '../shared/util';
import { MessageService } from '../shared/message.service';
import { MatRadioChange } from '@angular/material/radio';

@Component({
  selector: 'app-page4',
  template: `
    <h1>Page 4 - form controls in child components</h1>

    <app-menu></app-menu>

    <form [formGroup]="form">
      <div class="fields">
        <label>First name</label>
        <input appInput #first [formControl]="firstName" />
        <app-field-errors [control]="firstName"></app-field-errors>

        <label>Last name</label>
        <input appInput #last [formControl]="lastName" />
        <app-field-errors [control]="lastName"></app-field-errors>
      </div>

      <div class="select-sub">
        <mat-radio-group [value]="selectedSub()" (change)="subChanged($event)">
          <mat-radio-button value="sub1">Sub 1</mat-radio-button>
          <mat-radio-button value="sub2">Sub 2</mat-radio-button>
          <mat-radio-button value="sub3">Sub 3</mat-radio-button>
        </mat-radio-group>
      </div>

      <app-sub-1 *ngIf="sub === 'sub1'"></app-sub-1>
      <app-sub-2 *ngIf="sub === 'sub2'"></app-sub-2>
      <app-sub-3 *ngIf="sub === 'sub3'"></app-sub-3>

      <button mat-raised-button (click)="submit()" color="primary">
        Submit
      </button>
    </form>

    <div class="debug">
      <p>Debug:</p>
      <ul>
        <li>form.valid = {{ form.valid }}</li>
        <li>form.errors = {{ util.json(form.errors) }}</li>
        <li>form.controls = {{ controlNames() }}</li>
      </ul>
    </div>
  `,
  styles: [
    `
      .select-sub {
        margin: 1em;
      }

      mat-radio-group :not(:last-child) {
        padding-right: 1em;
      }

      button {
        margin-top: 1em;
      }

      form {
        padding-top: 2em;
      }

      .debug {
        padding-top: 4em;
        font-family: monospace;
      }
    `,
  ],
})
export class Page4Component {
  readonly util = Util;
  readonly firstName = new FormControl('', Validators.required);
  readonly lastName = new FormControl('', Validators.required);

  readonly form = new FormGroup({
    firstName: this.firstName,
    lastName: this.lastName,
  });

  sub = 'sub1';

  constructor(
    private messageService: MessageService,
    private cdr: ChangeDetectorRef
  ) {}

  submit(): void {
    if (this.form.valid) {
      this.messageService.say('Valid form submitted');
    } else {
      this.messageService.warn('Cannot submit invalid form');
    }
  }

  selectedSub(): string {
    return this.sub;
  }

  subChanged(event: MatRadioChange) {
    this.sub = event.value;
    this.form.updateValueAndValidity();
    this.cdr.detectChanges();
  }

  controlNames() {
    const controls = [];
    for (const key in this.form.controls) {
      if (this.form.controls.hasOwnProperty(key)) {
        controls.push(key);
      }
    }
    return controls.join(', ');
  }
}
