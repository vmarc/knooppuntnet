import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Tag } from '@api/custom/tag';

@Component({
  selector: 'kpn-tags-text',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (tag of tags(); track tag) {
      <div>{{ tag.key }} = {{ tag.value }}</div>
    } @empty {
      <ng-container i18n="@@tags.no-tags" class="no-tags">No tags</ng-container>
    }
  `,
  styles: `
    .no-tags {
      padding-top: 10px;
      padding-bottom: 10px;
    }
  `,
  imports: [],
})
export class TagsTextComponent {
  tags = input.required<Tag[]>();
}
