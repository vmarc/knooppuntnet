import { AsyncPipe } from '@angular/common';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { TagDiffs } from '@api/common/diff';
import { PageWidthService } from '@app/components/shared';
import { TagDiffsTableComponent } from './tag-diffs-table.component';
import { TagDiffsTextComponent } from './tag-diffs-text.component';

@Component({
  selector: 'kpn-tag-diffs',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (small()) {
      <kpn-tag-diffs-text [tagDiffs]="tagDiffs()" />
    } @else {
      <div class="kpn-label" i18n="@@tag-diffs.title">Tag changes</div>
      <kpn-tag-diffs-table #large [tagDiffs]="tagDiffs()" />
    }
  `,
  standalone: true,
  imports: [TagDiffsTextComponent, TagDiffsTableComponent, AsyncPipe],
})
export class TagDiffsComponent {
  tagDiffs = input.required<TagDiffs>();

  private readonly pageWidthService = inject(PageWidthService);
  protected readonly small = computed(() => this.pageWidthService.isAllSmall());
}
