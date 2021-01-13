import {Component} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {FormControl} from '@angular/forms';
import {Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {take} from 'rxjs/operators';
import {MessageService} from '../shared/message.service';
import {AppState} from '../store/app.state';
import {selectPage6State} from './store/page6.selectors';

@Component({
  selector: 'app-page6',
  template: `
    <h1>Page 6 - form ngrx</h1>

    <app-menu></app-menu>

    <form [formGroup]="form">

      <div class="fields">

        <label>Field 1</label>
        <input ourOwnInput [formControl]="field1">
        <app-field-errors [control]="field1"></app-field-errors>

        <label>Field 2</label>
        <input ourOwnInput [formControl]="field2">
        <app-field-errors [control]="field2"></app-field-errors>

        <label>Field 3</label>
        <input ourOwnInput [formControl]="field3">
        <app-field-errors [control]="field3"></app-field-errors>

        <label>Field 4</label>
        <input ourOwnInput [formControl]="field4">
        <app-field-errors [control]="field4"></app-field-errors>
      </div>

      <button mat-raised-button (click)="submit()" color="primary">Submit</button>
    </form>
  `
})
export class Page6Component {

  readonly field1 = new FormControl('', Validators.required);
  readonly field2 = new FormControl('', Validators.required);
  readonly field3 = new FormControl('', Validators.required);
  readonly field4 = new FormControl('', Validators.required);

  readonly form = new FormGroup({
    field1: this.field1,
    field2: this.field2,
    field3: this.field3,
    field4: this.field4
  });

  constructor(private store: Store<AppState>,
              private messageService: MessageService) {
    this.store.select(selectPage6State).pipe(take(1)).subscribe(value => this.form.patchValue(value, { emitEvent: false }));
  }

  submit(): void {
    if (this.form.valid) {
      this.messageService.say('Valid form submitted');
    } else {
      this.messageService.warn('Cannot submit invalid form');
    }
  }

}
