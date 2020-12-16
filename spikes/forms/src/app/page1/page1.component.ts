import {Component} from '@angular/core';
import {ViewChild} from '@angular/core';
import {FormGroupDirective} from '@angular/forms';
import {FormGroup} from '@angular/forms';
import {FormControl} from '@angular/forms';
import {Validators} from '@angular/forms';
import {MatFormField} from '@angular/material/form-field';
import {MessageService} from '../shared/message.service';
import {Util} from '../shared/util';

@Component({
  selector: 'app-page1',
  template: `
    <h1>Page 1 - form material field validation</h1>

    <app-menu></app-menu>

    <form [formGroup]="form">

      <p>
        <mat-form-field #firstNameFormField>
          <mat-label>First name</mat-label>
          <input matInput [formControl]="firstName">
        </mat-form-field>
      </p>

      <p>
        <mat-form-field #lastNameFormField>
          <mat-label>Last name</mat-label>
          <input matInput [formControl]="lastName">
        </mat-form-field>
      </p>

      <p>
        <mat-checkbox [formControl]="selected">
          Selected
        </mat-checkbox>
      </p>

      <button mat-raised-button (click)="submit()" color="primary" class="form-button">Submit</button>
      <button mat-raised-button (click)="formGroupReset()" class="form-button">FormGroup Reset</button>
    </form>
    <button mat-raised-button (click)="formDirectiveReset()" class="form-button">FormDirective Reset</button>

    <div class="debug">
      <p>
        Debug:
      </p>
      <ul>
        <li>form.valid = {{form.valid}}</li>
        <li>form.pristine = {{form.pristine}}</li>
        <li>form.touched = {{form.touched}}</li>
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
        <li>selected
          <ul>
            <li>valid = {{selected.valid}}</li>
            <li>pristine = {{selected.pristine}}</li>
            <li>touched = {{selected.touched}}</li>
            <li>errors = {{util.json(selected.errors)}}</li>
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

    .debug {
      padding-top: 4em;
      font-family: monospace;
    }

    .classes {
      padding-top: 2em;
    }

    .form-button {
      display: block;
      margin-bottom: 0.5em;
    }

    .classes button {
      margin-bottom: 1em;
    }
  `]
})
export class Page1Component {

  readonly firstName = new FormControl('', [this.firstNameValidator(), Validators.required]);
  readonly lastName = new FormControl('', [this.lastNameValidator(), Validators.required]);
  readonly selected = new FormControl(null, [Validators.required]);

  readonly form = new FormGroup({
    firstName: this.firstName,
    lastName: this.lastName,
    selected: this.selected
  });

  @ViewChild(FormGroupDirective) formGroupDirective: FormGroupDirective;

  constructor(private messageService: MessageService) {
  }

  submit(): void {
    if (this.form.valid) {
      this.messageService.say('submit valid form');
    } else {
      this.messageService.warn('cannot submit invalid form');
    }
  }

  formGroupReset(): void {
    this.form.reset();
  }

  formDirectiveReset(): void {
    const currentFormValues = this.form.value;
    this.formGroupDirective.resetForm();
    this.form.setValue(currentFormValues);
  }

  firstNameValidator() {
    return () => {
      console.log('validating first name: ' + this.firstName?.value);
      return null;
    };
  }

  lastNameValidator() {
    return () => {
      console.log('validating last name: ' + this.lastName?.value);
      return null;
    };
  }

  // ***************************** debug *****************************

  util = Util;

  firstNameClasses1 = '';
  lastNameClasses1 = '';
  firstNameClasses2 = '';
  lastNameClasses2 = '';

  @ViewChild('firstNameFormField') firstNameFormField: MatFormField;
  @ViewChild('lastNameFormField') lastNameFormField: MatFormField;

  updateClasses1(): void {
    this.firstNameClasses1 = this.firstNameFormField._elementRef.nativeElement.classList.value;
    this.lastNameClasses1 = this.lastNameFormField._elementRef.nativeElement.classList.value;
  }

  updateClasses2(): void {
    this.firstNameClasses2 = this.firstNameFormField._elementRef.nativeElement.classList.value;
    this.lastNameClasses2 = this.lastNameFormField._elementRef.nativeElement.classList.value;
  }

}
