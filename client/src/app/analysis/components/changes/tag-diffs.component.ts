import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {TagDiffs} from "../../../kpn/api/common/diff/tag-diffs";
import {PageWidthService} from "../../../components/shared/page-width.service";

@Component({
  selector: "kpn-tag-diffs",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="!!tagDiffs">
      <kpn-tag-diffs-text *ngIf="isSmall()" [tagDiffs]="tagDiffs"></kpn-tag-diffs-text>
      <kpn-tag-diffs-table *ngIf="!isSmall()" [tagDiffs]="tagDiffs"></kpn-tag-diffs-table>
    </div>
  `
})
export class TagDiffsComponent {

  @Input() tagDiffs: TagDiffs;

  constructor(private pageWidthService: PageWidthService) {
  }

  isSmall(): boolean {
    return this.pageWidthService.isSmall();
  }

}
