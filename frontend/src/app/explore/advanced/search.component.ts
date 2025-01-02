import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { DividerComponent } from '@app/components/shared';
import { ConditionTreeComponent } from '../condition/condition-tree.component';

@Component({
  selector: 'kpn-search',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-condition-tree />
    <div class="kpn-spacer-above kpn-spacer-below">
      <kpn-divider />
    </div>
  `,
  imports: [ConditionTreeComponent, DividerComponent],
})
export class SearchComponent {}
