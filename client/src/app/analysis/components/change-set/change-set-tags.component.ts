import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Tag} from "../../../kpn/shared/data/tag";
import {Tags} from "../../../kpn/shared/data/tags";

@Component({
  selector: "kpn-change-set-tags",
  template: `
    <div *ngIf="hasTags()">
      <div class="tags">
        <div *ngFor="let tag of tags()" class="tag">
          {{tag.key}} = {{tag.value}}
        </div>
      </div>
    </div>
  `,
  styles: [`

    .tags {
      padding-top: 5px;
      padding-left: 15px;
      padding-bottom: 25px;
    }

    .tag {
      padding-left: 5px;
      border-left-width: 1px;
      border-left-style: solid;
      border-left-color: #ccc;
      color: gray;
    }

  `]
})
export class ChangeSetTagsComponent {

  @Input() changeSetTags: Tags;

  tags(): List<Tag> {
    if (this.changeSetTags && this.changeSetTags.tags) {
      return this.changeSetTags.tags.filterNot(tag => tag.key == "comment");
    }
    return List();
  }

  hasTags(): boolean {
    return !this.tags().isEmpty();
  }

}
