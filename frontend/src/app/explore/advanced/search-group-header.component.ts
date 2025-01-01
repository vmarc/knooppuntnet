import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconButton } from '@angular/material/button';
import { MatButtonToggleChange } from '@angular/material/button-toggle';
import { MatButtonToggle } from '@angular/material/button-toggle';
import { MatButtonToggleGroup } from '@angular/material/button-toggle';
import { MatLabel } from '@angular/material/form-field';
import { MatIcon } from '@angular/material/icon';
import { MatMenuItem } from '@angular/material/menu';
import { MatMenuTrigger } from '@angular/material/menu';
import { MatMenu } from '@angular/material/menu';
import { ReactiveFormsModule } from '@angular/forms';
import { Condition } from '@api/common/search/condition';
import { ConditionGroup } from '@api/common/search/condition-group';
import { ExploreState } from '@app/state';

@Component({
  selector: 'kpn-search-group-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="group">
      <mat-button-toggle-group
        name="operator"
        [value]="group().operator"
        (change)="updateOperator($event)"
      >
        <mat-button-toggle value="and"> and</mat-button-toggle>
        <mat-button-toggle value="or">or</mat-button-toggle>
      </mat-button-toggle-group>

      <button mat-icon-button [matMenuTriggerFor]="addMenu">
        <mat-icon svgIcon="add"></mat-icon>
      </button>
      <mat-menu #addMenu="matMenu">
        <button mat-menu-item (click)="addCondition()">
          <mat-icon svgIcon="add"></mat-icon>
          <mat-label>add condition</mat-label>
        </button>
        <button mat-menu-item (click)="addGroup()">
          <mat-icon svgIcon="add"></mat-icon>
          <mat-label>add group</mat-label>
        </button>
      </mat-menu>
      @if (removeEnabled()) {
        <button mat-icon-button>
          <mat-icon svgIcon="remove" (click)="onRemove()"></mat-icon>
        </button>
      }
    </div>
  `,
  styles: `
    .group {
      padding-top: 0.5em;
      display: flex;
      align-items: center;
    }
  `,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatIcon,
    MatButtonToggleGroup,
    MatButtonToggle,
    MatIconButton,
    MatMenu,
    MatMenuTrigger,
    MatMenuItem,
    MatLabel,
  ],
})
export class SearchGroupHeaderComponent {
  group = input.required<ConditionGroup>();
  removeEnabled = input<boolean>(true);
  add = output<Condition>();
  remove = output<void>();
  update = output<ConditionGroup>();

  updateOperator(changeEvent: MatButtonToggleChange): void {
    this.update.emit({
      ...this.group(),
      operator: changeEvent.value,
    });
  }

  onRemove() {
    this.remove.emit();
  }

  addCondition() {
    this.add.emit(ExploreState.defaultCondition());
  }

  addGroup() {
    this.add.emit(ExploreState.defaultGroupCondition());
  }
}
