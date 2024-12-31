import { output } from '@angular/core';
import { computed } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatOption } from '@angular/material/autocomplete';
import { MatInput } from '@angular/material/input';
import { MatSelect } from '@angular/material/select';
import { MatFormField } from '@angular/material/select';
import { MatLabel } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { Condition } from '@api/common/search/condition';
import { ConditionRouteName } from '@api/common/search/condition-route-name';

@Component({
  selector: 'kpn-search-condition-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-form-field appearance="outline">
      <mat-label>operation</mat-label>
      <mat-select [value]="operator()">
        <mat-option value="equals">equals</mat-option>
        <mat-option value="contains">contains</mat-option>
      </mat-select>
    </mat-form-field>
    <mat-form-field appearance="outline">
      <mat-label>Route name</mat-label>
      <input matInput [value]="name()" (change)="onNameChange($event)" />
    </mat-form-field>
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
export class SearchConditionNameComponent {
  condition = input.required<Condition>();
  operator = computed(() => this.condition().name?.operator);
  name = computed(() => this.condition().name?.name);
  change = output<Condition>();

  onNameChange(event): void {
    console.log(`NAME CHANGE ${event.target.value}`);

    const name: ConditionRouteName = {
      ...this.condition().name,
      name: event.target.value,
    };
    const updatedCondition: Condition = {
      ...this.condition(),
      name,
    };
    this.change.emit(updatedCondition);
  }
}
