import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
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
import { ConditionOperator } from '@api/common/search/condition-operator';
import { Subscriptions } from '@app/util';

@Component({
  selector: 'kpn-search-condition-tag',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <form [formGroup]="form">
      <mat-form-field appearance="outline">
        <mat-label>tag key</mat-label>
        <input
          type="text"
          placeholder="key"
          matInput
          [formControl]="key"
          [matAutocomplete]="auto"
        />
        <mat-autocomplete autoActiveFirstOption #auto="matAutocomplete">
          <mat-option value="operator">operator</mat-option>
          <mat-option value="symbol">symbol</mat-option>
          <mat-option value="option3">option3</mat-option>
          <mat-option value="option4">option4</mat-option>
        </mat-autocomplete>
      </mat-form-field>

      <mat-form-field appearance="outline">
        <mat-label>operation</mat-label>
        <mat-select [formControl]="operator">
          <mat-option value="equals">equals</mat-option>
          <mat-option value="contains">contains</mat-option>
        </mat-select>
      </mat-form-field>
      <mat-form-field appearance="outline">
        <mat-label>Tag value</mat-label>
        <input matInput [formControl]="value" />
      </mat-form-field>
    </form>
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
export class SearchConditionTagComponent implements OnInit, OnDestroy {
  condition = input.required<Condition>();
  conditionChange = output<Condition>();

  private readonly subscriptions = new Subscriptions();
  readonly operator = new FormControl<ConditionOperator>('equals');
  readonly key = new FormControl<string>('');
  readonly value = new FormControl<string>('');
  readonly form = new FormGroup({
    operator: this.operator,
    key: this.key,
    value: this.value,
  });

  ngOnInit(): void {
    this.operator.setValue(this.condition().tag?.operator);
    this.key.setValue(this.condition().tag?.key);
    this.value.setValue(this.condition().tag?.value);
    this.subscriptions.add(
      this.form.valueChanges.subscribe((value) => {
        this.conditionChange.emit({
          subject: 'tag',
          tag: {
            operator: value.operator,
            key: value.key,
            value: value.value,
          },
        });
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
