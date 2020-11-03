import {Component} from "@angular/core";
import {FormGroup} from "@angular/forms";
import {FormControl} from "@angular/forms";
import {Validators} from "@angular/forms";
import {Util} from "../shared/util";
import {MessageService} from "../shared/message.service";

@Component({
  selector: "app-page1",
  template: `
    <h1>Page 1 - form material field validation</h1>

    <app-menu></app-menu>

    <form [formGroup]="form">

      <p>
        <mat-form-field>
          <mat-label>First name</mat-label>
          <input matInput [formControl]="firstName" placeholder="First name placeholder">
        </mat-form-field>
      </p>

      <p>
        <mat-form-field>
          <mat-label>Last name</mat-label>
          <input matInput [formControl]="lastName" placeholder="Last name placeholder">
        </mat-form-field>
      </p>

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

    .debug {
      padding-top: 4em;
      font-family: monospace;
    }
  `]
})
export class Page1Component {

  util = Util;

  readonly firstName = new FormControl("", [this.firstNameValidator(), Validators.required]);
  readonly lastName = new FormControl("", [this.lastNameValidator(), Validators.required]);

  readonly form = new FormGroup({
    firstName: this.firstName,
    lastName: this.lastName,
  });

  constructor(private messageService: MessageService) {
  }

  submit(): void {
    if (this.form.valid) {
      this.messageService.say("submit valid form");
    } else {
      this.messageService.warn("cannot submit invalid form");
    }
  }

  firstNameValidator() {
    return () => {
      console.log("validating first name: " + this.firstName?.value);
      return null;
    };
  }

  lastNameValidator() {
    return () => {
      console.log("validating last name: " + this.lastName?.value);
      return null;
    };
  }

}
