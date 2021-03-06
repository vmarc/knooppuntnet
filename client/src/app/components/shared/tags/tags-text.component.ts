import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Tags } from '@api/custom/tags';

@Component({
  selector: 'kpn-tags-text',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ng-container
      *ngIf="tags.tags.length === 0"
      i18n="@@tags.no-tags"
      class="no-tags"
      >No tags</ng-container
    >

    <div *ngIf="tags.tags.length > 0">
      <div *ngFor="let tag of tags.tags">{{ tag.key }} = {{ tag.value }}</div>
    </div>
  `,
  styles: [
    `
      .no-tags {
        padding-top: 10px;
        padding-bottom: 10px;
      }
    `,
  ],
})
export class TagsTextComponent {
  @Input() tags: Tags;
}
