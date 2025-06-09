import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { TagDiffs } from '@api/common/diff/tag-diffs';
import { PageWidthService } from '@app/shared/components/page-width.service';
import { TagDiffsTableComponent } from './tag-diffs-table.component';
import { TagDiffsTextComponent } from './tag-diffs-text.component';

@Component({
  selector: 'ui-tag-diffs',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (small()) {
      <ui-tag-diffs-text [tagDiffs]="tagDiffs()" />
    } @else {
      <div class="kpn-label" i18n="@@tag-diffs.title">Tag changes</div>
      <ui-tag-diffs-table #large [tagDiffs]="tagDiffs()" />
    }
  `,
  imports: [TagDiffsTextComponent, TagDiffsTableComponent],
})
export class TagDiffsComponent {
  readonly tagDiffs = input.required<TagDiffs>();

  private readonly pageWidthService = inject(PageWidthService);
  protected readonly small = computed(() => this.pageWidthService.isAllSmall());
}
