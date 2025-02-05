import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { DividerComponent } from '@app/shared/components/divider.component';
import { ConditionTreeComponent } from '../condition/condition-tree.component';
import { SearchRoutesComponent } from './search-routes.component';

@Component({
  selector: 'kpn-search',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-condition-tree />
    <div class="kpn-spacer-above kpn-spacer-below">
      <kpn-divider />
    </div>
    <kpn-search-routes />
  `,
  imports: [ConditionTreeComponent, DividerComponent, SearchRoutesComponent],
})
export class SearchComponent {}
