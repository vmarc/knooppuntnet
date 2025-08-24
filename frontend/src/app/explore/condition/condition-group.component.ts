import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { Condition } from '@api/common/search/condition';
import { ConditionGroup } from '@api/common/search/condition-group';
import { ExploreState } from '@app/state/explore-state';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzDropDownDirective } from 'ng-zorro-antd/dropdown';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzMenuItemComponent } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { NzRadioComponent } from 'ng-zorro-antd/radio';
import { NzRadioGroupComponent } from 'ng-zorro-antd/radio';
import { ConditionGroupForm } from './condition-controls';

@Component({
  selector: 'ui-condition-group',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="group">
      <nz-radio-group [formControl]="form().controls.operator" nzButtonStyle="solid">
        <label nz-radio-button nzValue="and">and</label>
        <label nz-radio-button nzValue="or">or</label>
      </nz-radio-group>

      <button nz-dropdown nz-button nzShape="circle" [nzDropdownMenu]="menu">
        <nz-icon nzType="plus" />
      </button>

      <nz-dropdown-menu #menu="nzDropdownMenu">
        <ul nz-menu>
          <li nz-menu-item (click)="addCondition()">
            <nz-icon nzType="plus" />
            <span>add condition</span>
          </li>
          <li nz-menu-item (click)="addGroup()">
            <nz-icon nzType="plus" />
            <span>add group</span>
          </li>
        </ul>
      </nz-dropdown-menu>
      @if (removeEnabled()) {
        <button nz-dropdown nz-button nzShape="circle" (click)="onRemove()">
          <nz-icon nzType="close" />
        </button>
      }
    </div>
  `,
  styles: `
    .group {
      padding-top: 0.5em;
      display: flex;
      align-items: center;
      gap: 1em;
    }

    li nz-icon {
      padding-right: 0.5em;
    }
  `,
  imports: [
    FormsModule,
    NzButtonComponent,
    NzDropDownDirective,
    NzDropdownMenuComponent,
    NzIconDirective,
    NzMenuDirective,
    NzMenuItemComponent,
    NzRadioComponent,
    NzRadioGroupComponent,
    ReactiveFormsModule,
  ],
})
export class ConditionGroupComponent {
  readonly form = input.required<ConditionGroupForm>();
  readonly removeEnabled = input<boolean>(true);
  readonly add = output<Condition>();
  readonly remove = output<void>();
  readonly update = output<ConditionGroup>();

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
