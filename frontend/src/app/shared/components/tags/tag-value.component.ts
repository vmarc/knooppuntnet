import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Tag } from '@api/custom/tag';

@Component({
  selector: 'kpn-tag-value',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @switch (tag().key) {
      @case ('website') {
        <a [href]="tag().value">{{ tag().value }}</a>
      }
      @case ('wikidata') {
        <a [href]="'http://www.wikidata.org/entity/' + tag().value">{{ tag().value }}</a>
      }
      @case ('wikipedia') {
        <a [href]="'https://en.wikipedia.org/wiki/' + tag().value">{{ tag().value }}</a>
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
  tag = input.required<Tag>();
}
