import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { TagDetail } from '@api/common/diff';
import { TagDiffs } from '@api/common/diff';

@Component({
  selector: 'kpn-tag-diffs-text',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (deletedTagDetails.length > 0) {
      <div class="important-title">
        @if (deletedTagDetails.length === 1) {
          <span class="kpn-label" i18n="@@tag-diffs.deleted-tag">Deleted tag</span>
        } @else {
          <span class="kpn-label" i18n="@@tag-diffs.deleted-tags">Deleted tags</span>
        }
      </div>

      @for (tagDetail of deletedTagDetails; track $index) {
        <div class="tag-detail">
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.key">Key</span>
            </div>
            <div class="important-value">{{ tagDetail.key }}</div>
          </div>
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.value">Value</span>
            </div>
            <div class="important-value">{{ tagDetail.valueBefore }}</div>
          </div>
        </div>
      }
    }

    @if (addedTagDetails.length > 0) {
      <div class="important-title">
        @if (addedTagDetails.length === 1) {
          <span class="kpn-label" i18n="@@tag-diffs.added-tag">Added tag</span>
        } @else {
          <span class="kpn-label" i18n="@@tag-diffs.added-tags">Added tags</span>
        }
      </div>

      @for (tagDetail of addedTagDetails; track $index) {
        <div class="tag-detail">
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.key">Key</span>
            </div>
            <div class="important-value">{{ tagDetail.key }}</div>
          </div>
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.value">Value</span>
            </div>
            <div class="important-value">{{ tagDetail.valueAfter }}</div>
          </div>
        </div>
      }
    }

    @if (updatedTagDetails.length > 0) {
      <div class="important-title">
        @if (updatedTagDetails.length === 1) {
          <span class="kpn-label" i18n="@@tag-diffs.updated-tag">Updated tag</span>
        } @else {
          <span class="kpn-label" i18n="@@tag-diffs.updated-tags">Updated tags</span>
        }
      </div>

      @for (tagDetail of updatedTagDetails; track $index) {
        <div class="tag-detail">
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.key">Key</span>
            </div>
            <div class="important-value">{{ tagDetail.key }}</div>
          </div>
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.before">Before</span>
            </div>
            <div class="important-value">{{ tagDetail.valueBefore }}</div>
          </div>
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.after">After</span>
            </div>
            <div class="important-value">{{ tagDetail.valueAfter }}</div>
          </div>
        </div>
      }
    }

    @if (sameTagDetails.length > 0) {
      <div class="title">
        @if (sameTagDetails.length === 1) {
          <span class="kpn-label" i18n="@@tag-diffs.same-tag">Same tag</span>
        } @else {
          <span class="kpn-label" i18n="@@tag-diffs.same-tags">Same tags</span>
        }
      </div>

      @for (tagDetail of sameTagDetails; track $index) {
        <div class="tag-detail">
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.key">Key</span>
            </div>
            <div class="value">{{ tagDetail.key }}</div>
          </div>
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.before">Before</span>
            </div>
            <div class="value">{{ tagDetail.valueBefore }}</div>
          </div>
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.after">After</span>
            </div>
            <div class="value">{{ tagDetail.valueAfter }}</div>
          </div>
        </div>
      }
    }
  `,
  styles: `
    .title {
      color: grey;
    }

    .tag-detail {
      margin-top: 3px;
      margin-bottom: 10px;
      border-left-style: dotted;
      border-left-width: 1px;
      border-left-color: grey;
    }

    .label {
      display: inline-block;
      color: grey;
      margin-left: 5px;
      width: 60px;
    }

    .important-value {
      display: inline-block;
    }

    .value {
      display: inline-block;
      color: grey;
    }
  `,
  standalone: true,
})
export class TagDiffsTextComponent implements OnInit {
  tagDiffs = input<TagDiffs | undefined>();

  deletedTagDetails: TagDetail[];
  addedTagDetails: TagDetail[];
  updatedTagDetails: TagDetail[];
  sameTagDetails: TagDetail[];

  ngOnInit(): void {
    const allTagDetails = this.tagDiffs().mainTags.concat(this.tagDiffs().extraTags);
    this.deletedTagDetails = allTagDetails.filter(
      (tagDetail) => tagDetail.action.name === 'Delete' /*TagDetailType.Delete*/
    );
    this.addedTagDetails = allTagDetails.filter(
      (tagDetail) => tagDetail.action.name === 'Add' /* TagDetailType.Add*/
    );
    this.updatedTagDetails = allTagDetails.filter(
      (tagDetail) => tagDetail.action.name === 'Update' /* TagDetailType.Update*/
    );
    this.sameTagDetails = allTagDetails.filter(
      (tagDetail) => tagDetail.action.name === 'Same' /* TagDetailType.Same*/
    );
  }
}
