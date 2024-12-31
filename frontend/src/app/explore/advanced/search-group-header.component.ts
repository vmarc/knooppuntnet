import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconButton } from '@angular/material/button';
import { MatButtonToggleChange } from '@angular/material/button-toggle';
import { MatButtonToggle } from '@angular/material/button-toggle';
import { MatButtonToggleGroup } from '@angular/material/button-toggle';
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
    <div>
      <mat-button-toggle-group
        name="operator"
        [value]="group().operator"
        (change)="updateOperator($event)"
      >
        <mat-button-toggle value="and">and</mat-button-toggle>
        <mat-button-toggle value="or">or</mat-button-toggle>
      </mat-button-toggle-group>

      <button mat-icon-button [matMenuTriggerFor]="addMenu">
        <mat-icon svgIcon="add"></mat-icon>
      </button>
      <mat-menu #addMenu="matMenu">
        <button mat-menu-item (click)="addCondition()">add condition</button>
        <button mat-menu-item (click)="addGroup()">add group</button>
      </mat-menu>
      @if (removeEnabled()) {
        <button mat-icon-button>
          <mat-icon svgIcon="remove" (click)="onRemove()"></mat-icon>
        </button>
      }
    </div>
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
  ],
})
export class SearchGroupHeaderComponent {
  group = input.required<ConditionGroup>();
  removeEnabled = input<boolean>(true);
  add = output<Condition>();
  remove = output<void>();
  update = output<ConditionGroup>();

  updateOperator(changeEvent: MatButtonToggleChange): void {
    const updatedGroup: ConditionGroup = {
      ...this.group(),
      operator: changeEvent.value,
    };
    console.log(`UPDATE OPERATOR ${JSON.stringify(updatedGroup, null, 2)}`);
    this.update.emit(updatedGroup);
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
