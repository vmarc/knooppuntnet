import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FormControlStatus } from '@angular/forms';
import { Observable } from 'rxjs';

@Component({
  selector: 'kpn-form-status',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if ((statusChanges() | async) === 'VALID') {
      <span [id]="formName() + '-valid'" class="form-valid">Valid</span>
    } @else {
      <span [id]="formName() + '-invalid'" class="form-valid">Invalid</span>
    }
  `,
  styles: `
    .form-valid {
      color: white;
    }
  `,
  standalone: true,
  imports: [AsyncPipe],
})
export class FormStatusComponent {
  formName = input.required<string>();
  statusChanges = input.required<Observable<FormControlStatus>>();
}
