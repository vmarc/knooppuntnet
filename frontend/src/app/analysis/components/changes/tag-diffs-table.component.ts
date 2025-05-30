import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { TagDiffs } from '@api/common/diff/tag-diffs';
import { TagDiffActionComponent } from './tag-diff-action.component';

@Component({
  selector: 'ui-tag-diffs-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (tagDiffs()) {
      <div class="title"></div>
      <table class="kpn-table" title="Tag differences" i18n-title="@@tag-diffs.table">
        <thead>
          <tr>
            <th></th>
            <th i18n="@@tag-diffs.key">Key</th>
            <th i18n="@@tag-diffs.before">Before</th>
            <th i18n="@@tag-diffs.after">After</th>
          </tr>
        </thead>
        <tbody>
          @for (tagDiff of tagDiffs().mainTags; track $index) {
            <tr [ngClass]="{ same: tagDiff.action === 'same' }">
              <td>
                <ui-tag-diff-action [action]="tagDiff.action" />
              </td>
              <td>{{ tagDiff.key }}</td>
              <td>{{ tagDiff.valueBefore }}</td>
              <td>{{ tagDiff.valueAfter }}</td>
            </tr>
          }

          @if (hasSeparator()) {
            <tr>
              <td colspan="4"></td>
            </tr>
          }

          @for (tagDiff of tagDiffs().extraTags; track $index) {
            <tr [ngClass]="{ same: tagDiff.action === 'same' }">
              <td>
                <ui-tag-diff-action [action]="tagDiff.action" />
              </td>
              <td>{{ tagDiff.key }}</td>
              <td>{{ tagDiff.valueBefore }}</td>
              <td>{{ tagDiff.valueAfter }}</td>
            </tr>
          }
        </tbody>
      </table>
    }
  `,
  styles: `
    .title {
      margin-top: 2px;
      margin-bottom: 4px;
    }

    .same {
      color: grey;
    }
  `,
  imports: [NgClass, TagDiffActionComponent],
})
export class TagDiffsTableComponent {
  tagDiffs = input.required<TagDiffs>();

  hasSeparator(): boolean {
    return this.tagDiffs().mainTags.length > 0 && this.tagDiffs().extraTags.length > 0;
  }
}
