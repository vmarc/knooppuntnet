import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { DividerComponent } from '@app/components/shared';
import { ConditionTreeComponent } from '../condition/condition-tree.component';
import { SearchTreeComponent } from './search-tree.component';
import { SearchService } from './search.service';

@Component({
  selector: 'kpn-search',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-condition-tree />
    <div class="kpn-spacer-above kpn-spacer-below">
      <kpn-divider />
    </div>
    <kpn-search-tree />
    <div class="kpn-spacer-above">
      <button mat-stroked-button>Search</button>
    </div>
    <pre>
    {{ groupString() }}
    </pre
    >
  `,
  imports: [
    MatButton,
    FormsModule,
    ReactiveFormsModule,
    SearchTreeComponent,
    ConditionTreeComponent,
    DividerComponent,
  ],
})
export class SearchComponent {
  private readonly service = inject(SearchService);
  readonly group = this.service.group;
  readonly groupString = computed(() => JSON.stringify(this.group(), null, 2));
}
