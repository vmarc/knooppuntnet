import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Tag } from '@api/custom';

@Component({
  selector: 'kpn-change-set-tags',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (hasTags()) {
      <div class="tags">
        @for (tag of tags(); track tag) {
          <div class="tag">{{ tag.key }} = {{ tag.value }}</div>
        }
      </div>
    }
  `,
  styles: `
    .tags {
      padding-top: 5px;
      padding-left: 15px;
      padding-bottom: 25px;
    }

    .tag {
      padding-left: 5px;
      border-left-width: 1px;
      border-left-style: solid;
      border-left-color: #ccc;
      color: grey;
    }
  `,
})
export class ChangeSetTagsComponent {
  changeSetTags = input.required<Tag[]>();

  tags(): Tag[] {
    if (this.changeSetTags()) {
      return this.changeSetTags().filter((tag) => tag.key !== 'comment');
    }
    return [];
  }

  hasTags(): boolean {
    return this.tags().length > 0;
  }
}
