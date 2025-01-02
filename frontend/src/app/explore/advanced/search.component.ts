import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatNavList } from '@angular/material/list';
import { MatListItem } from '@angular/material/list';
import { DividerComponent } from '@app/components/shared';
import { State } from '@app/state';
import { ConditionTreeComponent } from '../condition/condition-tree.component';

@Component({
  selector: 'kpn-search',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-condition-tree />
    <div class="kpn-spacer-above kpn-spacer-below">
      <kpn-divider />
    </div>
    <div>{{ rowCount() }} rows</div>
    <mat-nav-list>
      @for (result of results(); track result.id) {
        <mat-list-item>
          {{
            result.id +
              ' ' +
              result.name +
              ' ' +
              result.distance +
              ' ' +
              JSON.stringify(result.scopes)
          }}
          <kpn-divider />
        </mat-list-item>
      }
    </mat-nav-list>
    <kpn-divider />
  `,
  imports: [ConditionTreeComponent, DividerComponent, MatListItem, MatNavList],
})
export class SearchComponent {
  private readonly state = inject(State);
  readonly results = computed(() => this.state.explore.routeSearchResults());
  readonly rowCount = computed(() => this.results().length);
  protected readonly JSON = JSON;
}
