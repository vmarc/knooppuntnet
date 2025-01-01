import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { MatInput } from '@angular/material/input';
import { MatFormField } from '@angular/material/select';
import { MatLabel } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { Condition } from '@api/common/search/condition';
import { Subscriptions } from '@app/util';

@Component({
  selector: 'kpn-search-condition-location',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <form [formGroup]="form">
      <mat-form-field appearance="outline">
        <mat-label>name</mat-label>
        <input matInput [formControl]="name" />
      </mat-form-field>
    </form>
  `,
  imports: [MatLabel, MatFormField, MatInput, FormsModule, ReactiveFormsModule],
})
export class SearchConditionLocationComponent implements OnInit, OnDestroy {
  condition = input.required<Condition>();
  conditionChange = output<Condition>();

  private readonly subscriptions = new Subscriptions();
  readonly name = new FormControl<string>('');
  readonly form = new FormGroup({
    name: this.name,
  });

  ngOnInit(): void {
    this.name.setValue(this.condition().location?.name);
    this.subscriptions.add(
      this.form.valueChanges.subscribe((value) => {
        this.conditionChange.emit({
          subject: 'location',
          location: {
            operator: 'equals',
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
