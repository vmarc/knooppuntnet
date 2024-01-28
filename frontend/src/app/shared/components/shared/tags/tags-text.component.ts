import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Tags } from '@api/custom';

@Component({
  selector: 'kpn-tags-text',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (tags().tags.length === 0) {
      <ng-container i18n="@@tags.no-tags" class="no-tags">No tags()</ng-container>
    } @else {
      @for (tag of tags().tags; track tag) {
        <div>{{ tag.key }} = {{ tag.value }}</div>
      }
    }
  `,
  styles: `
    .no-tags {
      padding-top: 10px;
      padding-bottom: 10px;
    }
  `,
  standalone: true,
  imports: [],
})
export class TagsTextComponent {
  tags = input.required<Tags>();
}
