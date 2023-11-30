import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { TagDiffs } from '@api/common/diff';
import { TagDiffActionComponent } from './tag-diff-action.component';

@Component({
  selector: 'kpn-tag-diffs-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (tagDiffs) {
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
          @for (tagDetail of tagDiffs.mainTags; track $index) {
            <tr [ngClass]="{ same: tagDetail.action.name === 'Same' }">
              <td>
                <kpn-tag-diff-action [action]="tagDetail.action" />
              </td>
              <td>{{ tagDetail.key }}</td>
              <td>{{ tagDetail.valueBefore }}</td>
              <td>{{ tagDetail.valueAfter }}</td>
            </tr>
          }

          @if (hasSeparator()) {
            <tr>
              <td colspan="4"></td>
            </tr>
          }

          @for (tagDetail of tagDiffs.extraTags; track $index) {
            <tr [ngClass]="{ same: tagDetail.action.name === 'Same' }">
              <td>
                <kpn-tag-diff-action [action]="tagDetail.action" />
              </td>
              <td>{{ tagDetail.key }}</td>
              <td>{{ tagDetail.valueBefore }}</td>
              <td>{{ tagDetail.valueAfter }}</td>
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
  standalone: true,
  imports: [NgClass, TagDiffActionComponent],
})
export class TagDiffsTableComponent {
  @Input() tagDiffs: TagDiffs;

  hasSeparator(): boolean {
    return this.tagDiffs.mainTags.length > 0 && this.tagDiffs.extraTags.length > 0;
  }
}
