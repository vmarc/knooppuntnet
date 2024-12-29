import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { SearchTreeComponent } from './search-tree.component';
import { SearchService } from './search.service';

@Component({
  selector: 'kpn-search',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-search-tree />
    <div>
      <button mat-stroked-button>Search</button>
    </div>
    <pre>
    {{ groupString() }}
    </pre
    >
  `,
  imports: [MatButton, FormsModule, ReactiveFormsModule, SearchTreeComponent],
})
export class SearchComponent {
  private readonly service = inject(SearchService);
  readonly group = this.service.group;
  readonly groupString = computed(() => JSON.stringify(this.group(), null, 2));
}
