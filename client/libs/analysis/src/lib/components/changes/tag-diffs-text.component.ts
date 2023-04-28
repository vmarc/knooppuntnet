import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { TagDetail } from '@api/common/diff';
import { TagDiffs } from '@api/common/diff';

@Component({
  selector: 'kpn-tag-diffs-text',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="deletedTagDetails.length > 0">
      <div *ngIf="deletedTagDetails.length === 1" class="important-title">
        <span class="kpn-label" i18n="@@tag-diffs.deleted-tag"
          >Deleted tag</span
        >
      </div>
      <div *ngIf="deletedTagDetails.length > 1" class="important-title">
        <span class="kpn-label" i18n="@@tag-diffs.deleted-tags"
          >Deleted tags</span
        >
      </div>
      <div *ngFor="let tagDetail of deletedTagDetails" class="tag-detail">
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
    </div>

    <div *ngIf="addedTagDetails.length > 0">
      <div *ngIf="addedTagDetails.length === 1" class="important-title">
        <span class="kpn-label" i18n="@@tag-diffs.added-tag">Added tag</span>
      </div>
      <div *ngIf="addedTagDetails.length > 1" class="important-title">
        <span class="kpn-label" i18n="@@tag-diffs.added-tags">Added tags</span>
      </div>
      <div *ngFor="let tagDetail of addedTagDetails" class="tag-detail">
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
    </div>

    <div *ngIf="updatedTagDetails.length > 0">
      <div *ngIf="updatedTagDetails.length === 1" class="important-title">
        <span class="kpn-label" i18n="@@tag-diffs.updated-tag"
          >Updated tag</span
        >
      </div>
      <div *ngIf="updatedTagDetails.length > 1" class="important-title">
        <span class="kpn-label" i18n="@@tag-diffs.updated-tags"
          >Updated tags</span
        >
      </div>
      <div *ngFor="let tagDetail of updatedTagDetails" class="tag-detail">
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
    </div>

    <div *ngIf="sameTagDetails.length > 0">
      <div *ngIf="sameTagDetails.length === 1" class="title">
        <span class="kpn-label" i18n="@@tag-diffs.same-tag">Same tag</span>
      </div>
      <div *ngIf="sameTagDetails.length > 1" class="important-title">
        <span class="kpn-label" i18n="@@tag-diffs.same-tags">Same tags</span>
      </div>
      <div *ngFor="let tagDetail of sameTagDetails" class="tag-detail">
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
    </div>
  `,
  styles: [
    `
      .title {
        color: gray;
      }

      .tag-detail {
        margin-top: 3px;
        margin-bottom: 10px;
        border-left-style: dotted;
        border-left-width: 1px;
        border-left-color: gray;
      }

      .label {
        display: inline-block;
        color: gray;
        margin-left: 5px;
        width: 60px;
      }

      .important-value {
        display: inline-block;
      }

      .value {
        display: inline-block;
        color: gray;
      }
    `,
  ],
  standalone: true,
  imports: [NgIf, NgFor],
})
export class TagDiffsTextComponent implements OnInit {
  @Input() tagDiffs: TagDiffs;

  deletedTagDetails: TagDetail[];
  addedTagDetails: TagDetail[];
  updatedTagDetails: TagDetail[];
  sameTagDetails: TagDetail[];

  ngOnInit(): void {
    const allTagDetails = this.tagDiffs.mainTags.concat(
      this.tagDiffs.extraTags
    );
    this.deletedTagDetails = allTagDetails.filter(
      (tagDetail) => tagDetail.action.name === 'Delete' /*TagDetailType.Delete*/
    );
    this.addedTagDetails = allTagDetails.filter(
      (tagDetail) => tagDetail.action.name === 'Add' /* TagDetailType.Add*/
    );
    this.updatedTagDetails = allTagDetails.filter(
      (tagDetail) =>
        tagDetail.action.name === 'Update' /* TagDetailType.Update*/
    );
    this.sameTagDetails = allTagDetails.filter(
      (tagDetail) => tagDetail.action.name === 'Same' /* TagDetailType.Same*/
    );
  }
}
