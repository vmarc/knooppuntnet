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
import { MatInput } from '@angular/material/input';
import { MatSelect } from '@angular/material/select';
import { MatFormField } from '@angular/material/select';
import { MatLabel } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { Condition } from '@api/common/search/condition';
import { ConditionOperator } from '@api/common/search/condition-operator';
import { Subscriptions } from '@app/util';

@Component({
  selector: 'kpn-search-condition-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <form [formGroup]="form">
      <mat-form-field appearance="outline" class="operator">
        <mat-label>operation</mat-label>
        <mat-select [formControl]="operator">
          <mat-option value="equals">equals</mat-option>
          <mat-option value="contains">contains</mat-option>
        </mat-select>
      </mat-form-field>
      <mat-form-field appearance="outline">
        <mat-label>route name</mat-label>
        <input matInput [formControl]="name" />
      </mat-form-field>
    </form>
  `,
  styles: `
    .operator {
      width: 8em;
    }

    form {
      display: flex;
      align-items: center;
      gap: 0.5em;
    }
  `,
  imports: [
    MatLabel,
    MatFormField,
    MatInput,
    FormsModule,
    ReactiveFormsModule,
    MatOption,
    MatSelect,
  ],
})
export class SearchConditionNameComponent implements OnInit, OnDestroy {
  condition = input.required<Condition>();
  conditionChange = output<Condition>();

  private readonly subscriptions = new Subscriptions();
  readonly operator = new FormControl<ConditionOperator>('equals');
  readonly name = new FormControl<string>('');
  readonly form = new FormGroup({
    operator: this.operator,
    name: this.name,
  });

  ngOnInit(): void {
    this.operator.setValue(this.condition().name?.operator);
    this.name.setValue(this.condition().name?.name);
    this.subscriptions.add(
      this.form.valueChanges.subscribe((value) => {
        this.conditionChange.emit({
          subject: 'name',
          name: {
            operator: value.operator,
            name: value.name,
          },
        });
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
