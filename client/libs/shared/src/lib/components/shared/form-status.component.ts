import { AsyncPipe } from '@angular/common';
import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormControlStatus } from '@angular/forms';
import { Observable } from 'rxjs';

@Component({
  selector: 'kpn-form-status',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ng-container
      *ngIf="(statusChanges | async) === 'VALID'; then valid; else invalid"
    ></ng-container>
    <ng-template #valid>
      <span id="valid" class="form-valid">Valid</span>
    </ng-template>
    <ng-template #invalid>
      <span id="invalid" class="form-valid">Invalid</span>
    </ng-template>
  `,
  styles: [
    `
      .form-valid {
        color: white;
      }
    `,
  ],
  standalone: true,
  imports: [NgIf, AsyncPipe],
})
export class FormStatusComponent {
  @Input() statusChanges: Observable<FormControlStatus>;
}
