import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {ChangeSetInfo} from "../../../kpn/shared/changes/change-set-info";
import {Tag} from "../../../kpn/shared/data/tag";

@Component({
  selector: "kpn-change-set-info",
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
export class ChangeSetInfoComponent {

  @Input() changeSetInfo: ChangeSetInfo;

  tags(): List<Tag> {
    if (this.changeSetInfo && this.changeSetInfo.tags && this.changeSetInfo.tags.tags) {
      return this.changeSetInfo.tags.tags.filterNot(tag => tag.key == "comment");
    }
    return List();
  }

  hasTags(): boolean {
    return !this.tags().isEmpty();
  }

}
