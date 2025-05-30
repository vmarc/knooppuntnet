import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatInput } from '@angular/material/input';
import { MatFormField } from '@angular/material/select';
import { MatLabel } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { ConditionLocationForm } from './condition-controls';

@Component({
  selector: 'ui-condition-location',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-form-field appearance="outline">
      <mat-label>name</mat-label>
      <input matInput [formControl]="form().controls.name" />
    </mat-form-field>
  `,
  imports: [MatLabel, MatFormField, MatInput, FormsModule, ReactiveFormsModule],
})
export class ConditionLocationComponent {
  form = input.required<ConditionLocationForm>();
}
