import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { DividerComponent } from '@app/shared/components/divider.component';
import { ConditionTreeComponent } from '../condition/condition-tree.component';
import { SearchRoutesComponent } from './search-routes.component';

@Component({
  selector: 'ui-search',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-condition-tree />
    <div class="kpn-spacer-above kpn-spacer-below">
      <ui-divider />
    </div>
    <ui-search-routes />
  `,
  imports: [ConditionTreeComponent, DividerComponent, SearchRoutesComponent],
})
export class SearchComponent {}
