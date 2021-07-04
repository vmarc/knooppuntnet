import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { TagDiffs } from '@api/common/diff/tag-diffs';

@Component({
  selector: 'kpn-tag-diffs-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="!!tagDiffs">
      <div class="title"></div>

      <table
        class="kpn-table"
        title="Tag differences"
        i18n-title="@@tag-diffs.table"
      >
        <thead>
          <tr>
            <th></th>
            <th i18n="@@tag-diffs.key">Key</th>
            <th i18n="@@tag-diffs.before">Before</th>
            <th i18n="@@tag-diffs.after">After</th>
          </tr>
        </thead>
        <tbody>
          <tr
            *ngFor="let tagDetail of tagDiffs.mainTags"
            [ngClass]="{ same: tagDetail.action.name == 'Same' }"
          >
            <td>
              <kpn-tag-diff-action
                [action]="tagDetail.action"
              ></kpn-tag-diff-action>
            </td>
            <td>{{ tagDetail.key }}</td>
            <td>{{ tagDetail.valueBefore }}</td>
            <td>{{ tagDetail.valueAfter }}</td>
          </tr>

          <tr *ngIf="hasSeparator()">
            <td colspan="4"></td>
          </tr>

          <tr
            *ngFor="let tagDetail of tagDiffs.extraTags"
            [ngClass]="{ same: tagDetail.action.name == 'Same' }"
          >
            <td>
              <kpn-tag-diff-action
                [action]="tagDetail.action"
              ></kpn-tag-diff-action>
            </td>
            <td>{{ tagDetail.key }}</td>
            <td>{{ tagDetail.valueBefore }}</td>
            <td>{{ tagDetail.valueAfter }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  `,
  styles: [
    `
      .title {
        margin-top: 2px;
        margin-bottom: 4px;
      }

      .same {
        color: gray;
      }
    `,
  ],
})
export class TagDiffsTableComponent {
  @Input() tagDiffs: TagDiffs;

  hasSeparator(): boolean {
    return (
      !this.tagDiffs.mainTags.isEmpty() && !this.tagDiffs.extraTags.isEmpty()
    );
  }
}
