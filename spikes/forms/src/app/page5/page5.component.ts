import {Component} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {FormControl} from '@angular/forms';
import {Validators} from '@angular/forms';
import {Util} from '../shared/util';
import {MessageService} from '../shared/message.service';

@Component({
  selector: 'app-page5',
  template: `
    <h1>Page 5 - form controls in child component</h1>

    <app-menu></app-menu>

    <form [formGroup]="form">

      <div class="fields">

        <label>Field 1</label>
        <input ourOwnInput #first [formControl]="field1">
        <app-field-errors [control]="field1"></app-field-errors>

        <label>Field 2</label>
        <input ourOwnInput #last [formControl]="field2">
        <app-field-errors [control]="field2"></app-field-errors>
      </div>

      <app-sub-5></app-sub-5>

      <button mat-raised-button (click)="submit()" color="primary">Submit</button>
    </form>

    <div class="debug">
      <p>
        Debug:
      </p>
      <ul>
        <li>form.valid = {{form.valid}}</li>
        <li>form.errors = {{util.json(form.errors)}}</li>
        <li>
          form.controls = {{controlNames()}}
        </li>
      </ul>
    </div>
  `,
  styles: [`

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

    .fields {
      margin-bottom: 1em;
    }
  `]
})
export class Page5Component {

  readonly util = Util;
  readonly field1 = new FormControl('', Validators.required);
  readonly field2 = new FormControl('', Validators.required);
  readonly field3 = new FormControl('', Validators.required);
  readonly field4 = new FormControl('', Validators.required);

  readonly form = new FormGroup({
    field1: this.field1,
    field2: this.field2,
    field3: this.field3,
    field4: this.field4,
  });

  constructor(private messageService: MessageService) {
  }

  submit(): void {
    if (this.form.valid) {
      this.messageService.say('Valid form submitted');
    } else {
      this.messageService.warn('Cannot submit invalid form');
    }
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
