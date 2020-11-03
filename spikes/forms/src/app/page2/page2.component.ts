import {Component} from "@angular/core";
import {FormGroup} from "@angular/forms";
import {FormControl} from "@angular/forms";
import {Validators} from "@angular/forms";
import {Util} from "../shared/util";
import {MessageService} from "../shared/message.service";

@Component({
  selector: "app-page2",
  template: `
    <h1>Page 2 - form regular field validation</h1>

    <app-menu></app-menu>

    <form [formGroup]="form">

      <div class="fields">

        <label>First name</label>
        <input kpn-input #first [formControl]="firstName" placeholder="First name placeholder">
        <app-field-errors [control]="firstName"></app-field-errors>

        <label>Last name</label>
        <input kpn-input #last [formControl]="lastName" placeholder="Last name placeholder">
        <app-field-errors [control]="lastName"></app-field-errors>

      </div>

      <button mat-raised-button (click)="submit()" color="primary">Submit</button>
    </form>

    <div class="debug">
      <p>
        Debug:
      </p>
      <ul>
        <li>form.valid = {{form.valid}}</li>
        <li>form.errors = {{util.json(form.errors)}}</li>
        <li>firstName
          <ul>
            <li>valid = {{firstName.valid}}</li>
            <li>pristine = {{firstName.pristine}}</li>
            <li>touched = {{firstName.touched}}</li>
            <li>errors = {{util.json(firstName.errors)}}</li>
          </ul>
        </li>
        <li>lastName
          <ul>
            <li>valid = {{lastName.valid}}</li>
            <li>pristine = {{lastName.pristine}}</li>
            <li>touched = {{lastName.touched}}</li>
            <li>errors = {{util.json(lastName.errors)}}</li>
          </ul>
        </li>
      </ul>
    </div>
  `,
  styles: [`

    form {
      padding-top: 2em;
    }

    .fields {
      display: grid;
      grid-template-columns: 5em 20em auto;
      column-gap: 1em;
      row-gap: 1em;
    }

    .debug {
      padding-top: 4em;
      font-family: monospace;
    }

    form button {
      margin-top: 2em;
    }

    input.ng-invalid {
      border: 1px solid red;
      background: rgba(255, 182, 193, 0.2);
    }

    input.ng-invalid.ng-pristine {
      border: 1px solid rgb(118, 118, 118);
      background: none;
    }

    input.ng-invalid.kpn-submitted {
      border: 1px solid red;
      background: rgba(255, 182, 193, 0.2);
    }
  `]
})
export class Page2Component {

  util = Util;

  readonly firstName = new FormControl("", Validators.required);
  readonly lastName = new FormControl("", Validators.required);

  readonly form = new FormGroup({
    firstName: this.firstName,
    lastName: this.lastName,
  });

  constructor(private messageService: MessageService) {
  }

  submit(): void {
    Util.submitForm(this.form);
    if (this.form.valid) {
      this.messageService.say("Valid form submitted");
      Util.resetForm(this.form);
    } else {
      this.messageService.warn("Cannot submit invalid form");
    }
  }

}
