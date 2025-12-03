import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Tag } from '@api/custom/tag';

@Component({
  selector: 'ui-tag-value',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @switch (tag().key) {
      @case ('website') {
        <a [href]="tag().value">{{ tag().value }}</a>
      }
      @case ('wikidata') {
        <a [href]="wikidataLink()">{{ tag().value }}</a>
      }
      @case ('wikipedia') {
        <a [href]="wikipediaLink()">{{ tag().value }}</a>
      }
      @default {
        @if (tag().value.startsWith('http')) {
          <a [href]="tag().value">{{ tag().value }}</a>
        } @else {
          {{ tag().value }}
        }
      }
    }
  `,
})
export class TagValueComponent {
  readonly tag = input.required<Tag>();
  protected readonly wikidataLink = computed(
    () => `http://www.wikidata.org/entity/${this.tag().value}`
  );
  protected readonly wikipediaLink = computed(
    () => `https://en.wikipedia.org/wiki/${this.tag().value}`
  );
}
