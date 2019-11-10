import {Component, Input, OnInit} from "@angular/core";
import {TagDiffs} from "../../../kpn/api/common/diff/tag-diffs";
import {TagDetail} from "../../../kpn/api/common/diff/tag-detail";
import {List} from "immutable";

@Component({
  selector: "kpn-tag-diffs-text",
  template: `

    <div *ngIf="!deletedTagDetails.isEmpty()">
      <div *ngIf="deletedTagDetails.size == 1" class="important-title">
        <span i18n="@@tag-diffs.deleted-tag">Deleted tag</span>:
      </div>
      <div *ngIf="deletedTagDetails.size > 1" class="important-title">
        <span i18n="@@tag-diffs.deleted-tags">Deleted tags</span>:
      </div>
      <div *ngFor="let tagDetail of deletedTagDetails" class="tag-detail">
        <div>
          <div class="label">
            <span i18n="@@tag-diffs.key">Key</span>:
          </div>
          <div class="important-value">{{tagDetail.key}}</div>
        </div>
        <div>
          <div class="label">
            <span i18n="@@tag-diffs.value">Value</span>:
          </div>
          <div class="important-value">{{tagDetail.valueBefore}}</div>
        </div>
      </div>
    </div>


    <div *ngIf="!addedTagDetails.isEmpty()">
      <div *ngIf="addedTagDetails.size == 1" class="important-title">
        <span i18n="@@tag-diffs.added-tag">Added tag</span>:
      </div>
      <div *ngIf="addedTagDetails.size > 1" class="important-title">
        <span i18n="@@tag-diffs.added-tags">Added tags</span>:
      </div>
      <div *ngFor="let tagDetail of addedTagDetails" class="tag-detail">
        <div>
          <div class="label">
            <span i18n="@@tag-diffs.key">Key</span>:
          </div>
          <div class="important-value">{{tagDetail.key}}</div>
        </div>
        <div>
          <div class="label">
            <span i18n="@@tag-diffs.value">Value</span>:
          </div>
          <div class="important-value">{{tagDetail.valueAfter}}</div>
        </div>
      </div>
    </div>


    <div *ngIf="!updatedTagDetails.isEmpty()">
      <div *ngIf="updatedTagDetails.size == 1" class="important-title">
        <span i18n="@@tag-diffs.updated-tag">Updated tag</span>:
      </div>
      <div *ngIf="updatedTagDetails.size > 1" class="important-title">
        <span i18n="@@tag-diffs.updated-tags">Updated tags</span>:
      </div>
      <div *ngFor="let tagDetail of updatedTagDetails" class="tag-detail">
        <div>
          <div class="label">
            <span i18n="@@tag-diffs.key">Key</span>:
          </div>
          <div class="important-value">{{tagDetail.key}}</div>
        </div>
        <div>
          <div class="label">
            <span i18n="@@tag-diffs.before">Before</span>:
          </div>
          <div class="important-value">{{tagDetail.valueBefore}}</div>
        </div>
        <div>
          <div class="label">
            <span i18n="@@tag-diffs.after">After</span>:
          </div>
          <div class="important-value">{{tagDetail.valueAfter}}</div>
        </div>
      </div>
    </div>


    <div *ngIf="!sameTagDetails.isEmpty()">
      <div *ngIf="sameTagDetails.size == 1" class="title">
        <span i18n="@@tag-diffs.same-tag">Same tag</span>:
      </div>
      <div *ngIf="sameTagDetails.size > 1" class="important-title">
        <span i18n="@@tag-diffs.same-tags">Same tags</span>:
      </div>
      <div *ngFor="let tagDetail of sameTagDetails" class="tag-detail">
        <div>
          <div class="label">
            <span i18n="@@tag-diffs.key">Key</span>:
          </div>
          <div class="value">{{tagDetail.key}}</div>
        </div>
        <div>
          <div class="label">
            <span i18n="@@tag-diffs.before">Before</span>:
          </div>
          <div class="value">{{tagDetail.valueBefore}}</div>
        </div>
        <div>
          <div class="label">
            <span i18n="@@tag-diffs.after">After</span>:
          </div>
          <div class="value">{{tagDetail.valueAfter}}</div>
        </div>
      </div>
    </div>

  `,
  styles: [`

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

  `]
})
export class TagDiffsTextComponent implements OnInit {

  @Input() tagDiffs: TagDiffs;

  deletedTagDetails: List<TagDetail>;
  addedTagDetails: List<TagDetail>;
  updatedTagDetails: List<TagDetail>;
  sameTagDetails: List<TagDetail>;

  ngOnInit(): void {
    const allTagDetails = this.tagDiffs.mainTags.concat(this.tagDiffs.extraTags);
    this.deletedTagDetails = allTagDetails.filter(tagDetail => tagDetail.action.name == "Delete" /*TagDetailType.Delete*/);
    this.addedTagDetails = allTagDetails.filter(tagDetail => tagDetail.action.name == "Add" /* TagDetailType.Add*/);
    this.updatedTagDetails = allTagDetails.filter(tagDetail => tagDetail.action.name == "Update" /* TagDetailType.Update*/);
    this.sameTagDetails = allTagDetails.filter(tagDetail => tagDetail.action.name == "Same" /* TagDetailType.Same*/);
  }

}
