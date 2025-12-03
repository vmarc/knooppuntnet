import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { TagDiff } from '@api/common/diff/tag-diff';
import { TagDiffs } from '@api/common/diff/tag-diffs';

@Component({
  selector: 'ui-tag-diffs-text',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/cyclomatic-complexity -->
    @if (deletedTagDiffs.length > 0) {
      <div class="important-title">
        @if (deletedTagDiffs.length === 1) {
          <span class="kpn-label" i18n="@@tag-diffs.deleted-tag">Deleted tag</span>
        } @else {
          <span class="kpn-label" i18n="@@tag-diffs.deleted-tags">Deleted tags</span>
        }
      </div>

      @for (tagDiff of deletedTagDiffs; track $index) {
        <div class="tag-detail">
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.key">Key</span>
            </div>
            <div class="important-value">{{ tagDiff.key }}</div>
          </div>
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.value">Value</span>
            </div>
            <div class="important-value">{{ tagDiff.valueBefore }}</div>
          </div>
        </div>
      }
    }

    @if (addedTagDiffs.length > 0) {
      <div class="important-title">
        @if (addedTagDiffs.length === 1) {
          <span class="kpn-label" i18n="@@tag-diffs.added-tag">Added tag</span>
        } @else {
          <span class="kpn-label" i18n="@@tag-diffs.added-tags">Added tags</span>
        }
      </div>

      @for (tagDiff of addedTagDiffs; track $index) {
        <div class="tag-detail">
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.key">Key</span>
            </div>
            <div class="important-value">{{ tagDiff.key }}</div>
          </div>
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.value">Value</span>
            </div>
            <div class="important-value">{{ tagDiff.valueAfter }}</div>
          </div>
        </div>
      }
    }

    @if (updatedTagDiffs.length > 0) {
      <div class="important-title">
        @if (updatedTagDiffs.length === 1) {
          <span class="kpn-label" i18n="@@tag-diffs.updated-tag">Updated tag</span>
        } @else {
          <span class="kpn-label" i18n="@@tag-diffs.updated-tags">Updated tags</span>
        }
      </div>

      @for (tagDiff of updatedTagDiffs; track $index) {
        <div class="tag-detail">
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.key">Key</span>
            </div>
            <div class="important-value">{{ tagDiff.key }}</div>
          </div>
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.before">Before</span>
            </div>
            <div class="important-value">{{ tagDiff.valueBefore }}</div>
          </div>
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.after">After</span>
            </div>
            <div class="important-value">{{ tagDiff.valueAfter }}</div>
          </div>
        </div>
      }
    }

    @if (sameTagDiffs.length > 0) {
      <div class="title">
        @if (sameTagDiffs.length === 1) {
          <span class="kpn-label" i18n="@@tag-diffs.same-tag">Same tag</span>
        } @else {
          <span class="kpn-label" i18n="@@tag-diffs.same-tags">Same tags</span>
        }
      </div>

      @for (tagDiff of sameTagDiffs; track $index) {
        <div class="tag-detail">
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.key">Key</span>
            </div>
            <div class="value">{{ tagDiff.key }}</div>
          </div>
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.before">Before</span>
            </div>
            <div class="value">{{ tagDiff.valueBefore }}</div>
          </div>
          <div>
            <div class="label">
              <span class="kpn-label" i18n="@@tag-diffs.after">After</span>
            </div>
            <div class="value">{{ tagDiff.valueAfter }}</div>
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
})
export class TagDiffsTextComponent implements OnInit {
  readonly tagDiffs = input.required<TagDiffs>();

  deletedTagDiffs: TagDiff[];
  addedTagDiffs: TagDiff[];
  updatedTagDiffs: TagDiff[];
  sameTagDiffs: TagDiff[];

  ngOnInit(): void {
    const allTagDiffs = this.tagDiffs().mainTags.concat(this.tagDiffs().extraTags);
    this.deletedTagDiffs = allTagDiffs.filter((tagDiff) => tagDiff.action === 'delete');
    this.addedTagDiffs = allTagDiffs.filter((tagDiff) => tagDiff.action === 'add');
    this.updatedTagDiffs = allTagDiffs.filter((tagDiff) => tagDiff.action === 'update');
    this.sameTagDiffs = allTagDiffs.filter((tagDiff) => tagDiff.action === 'same');
  }
}
