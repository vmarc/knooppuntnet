import { computed } from '@angular/core';
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
import { Condition } from '@api/common/search/condition';

@Component({
  selector: 'kpn-search-condition-tag',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-form-field appearance="outline">
      <mat-label>tag key</mat-label>
      <input type="text" placeholder="Pick one" matInput [value]="key()" [matAutocomplete]="auto" />
      <mat-autocomplete autoActiveFirstOption #auto="matAutocomplete">
        <mat-option value="operator">operator</mat-option>
        <mat-option value="symbol">symbol</mat-option>
        <mat-option value="option3">option3</mat-option>
        <mat-option value="option4">option4</mat-option>
      </mat-autocomplete>
    </mat-form-field>

    <mat-form-field appearance="outline">
      <mat-label>operation</mat-label>
      <mat-select [value]="operator()">
        <mat-option value="equals">equals</mat-option>
        <mat-option value="contains">contains</mat-option>
      </mat-select>
    </mat-form-field>
    <mat-form-field appearance="outline">
      <mat-label>Tag value</mat-label>
      <input matInput [value]="value()" />
    </mat-form-field>
  `,
  styles: ``,
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
export class SearchConditionTagComponent {
  condition = input.required<Condition>();
  operator = computed(() => this.condition().tag?.operator);
  key = computed(() => this.condition().tag?.key);
  value = computed(() => this.condition().tag?.value);
}
