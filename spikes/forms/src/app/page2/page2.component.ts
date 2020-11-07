import {Component} from "@angular/core";
import {ViewChild} from "@angular/core";
import {ElementRef} from "@angular/core";
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
        <input our-own-input #first [formControl]="firstName">
        <app-field-errors [control]="firstName"></app-field-errors>

        <label>Last name</label>
        <input our-own-input #last [formControl]="lastName">
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
      <div class="classes">
        <button mat-raised-button (click)="updateClasses1()">Classes</button>
        <div *ngIf="firstNameClasses1">
          <p>
            firstName classes =
            <app-classes [classes]="firstNameClasses1"></app-classes>
          </p>
          <p>
            lastName classes =
            <app-classes [classes]="lastNameClasses1"></app-classes>
          </p>
        </div>
      </div>
      <div class="classes">
        <button mat-raised-button (click)="updateClasses2()">Classes</button>
        <div *ngIf="firstNameClasses2">
          <p>
            firstName classes =
            <app-classes [classes]="firstNameClasses2"></app-classes>
          </p>
          <p>
            lastName classes =
            <app-classes [classes]="lastNameClasses2"></app-classes>
          </p>
        </div>
      </div>
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

    input.ng-invalid.our-own-submitted {
      border: 1px solid red;
      background: rgba(255, 182, 193, 0.2);
    }

    .classes {
      padding-top: 2em;
    }

    .classes button {
      margin-bottom: 1em;
    }
  `]
})
export class Page2Component {

  readonly firstName = new FormControl("", Validators.required);
  readonly lastName = new FormControl("", Validators.required);

  readonly form = new FormGroup({
    firstName: this.firstName,
    lastName: this.lastName,
  });

  constructor(private messageService: MessageService) {
  }

  submit(): void {
    if (this.form.valid) {
      this.messageService.say("Valid form submitted");
    } else {
      this.messageService.warn("Cannot submit invalid form");
    }
  }

  // ***************************** debug *****************************

  util = Util;
  firstNameClasses1 = "";
  lastNameClasses1 = "";
  firstNameClasses2 = "";
  lastNameClasses2 = "";

  @ViewChild("first") firstNameInput: ElementRef;
  @ViewChild("last") lastNameInput: ElementRef;

  updateClasses1(): void {
    this.firstNameClasses1 = this.firstNameInput.nativeElement.classList.value;
    this.lastNameClasses1 = this.lastNameInput.nativeElement.classList.value;
  }

  updateClasses2(): void {
    this.firstNameClasses2 = this.firstNameInput.nativeElement.classList.value;
    this.lastNameClasses2 = this.lastNameInput.nativeElement.classList.value;
  }

}
