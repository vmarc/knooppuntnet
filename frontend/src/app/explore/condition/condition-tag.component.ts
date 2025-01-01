import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatOption } from '@angular/material/autocomplete';
import { MatAutocompleteTrigger } from '@angular/material/autocomplete';
import { MatAutocomplete } from '@angular/material/autocomplete';
import { MatInput } from '@angular/material/input';
import { MatSelect } from '@angular/material/select';
import { MatFormField } from '@angular/material/select';
import { MatLabel } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { ConditionTagForm } from './condition-controls';

@Component({
  selector: 'kpn-condition-tag',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-form-field appearance="outline">
      <mat-label>tag key</mat-label>
      <input
        type="text"
        placeholder="key"
        matInput
        [formControl]="form().controls.key"
        [matAutocomplete]="auto"
      />
      <mat-autocomplete autoActiveFirstOption #auto="matAutocomplete">
        <mat-option value="operator">operator</mat-option>
        <mat-option value="symbol">symbol</mat-option>
        <mat-option value="option3">option3</mat-option>
        <mat-option value="option4">option4</mat-option>
      </mat-autocomplete>
    </mat-form-field>

    <mat-form-field appearance="outline" class="operator">
      <mat-label>operation</mat-label>
      <mat-select [formControl]="form().controls.operator">
        <mat-option value="equals">equals</mat-option>
        <mat-option value="contains">contains</mat-option>
      </mat-select>
    </mat-form-field>
    <mat-form-field appearance="outline">
      <mat-label>tag value</mat-label>
      <input matInput [formControl]="form().controls.value" />
    </mat-form-field>
  `,
  styles: `
    form {
      display: flex;
      align-items: center;
      gap: 0.5em;
    }

    .operator {
      width: 8em;
    }
  `,
  imports: [
    MatLabel,
    MatFormField,
    MatInput,
    FormsModule,
    ReactiveFormsModule,
    MatAutocomplete,
    MatAutocompleteTrigger,
    MatOption,
    MatSelect,
  ],
})
export class ConditionTagComponent {
  form = input.required<ConditionTagForm>();
}
