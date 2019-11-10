import {Component, Input} from "@angular/core";
import {ChangeKey} from "../../../kpn/api/common/changes/details/change-key";
import {PageWidthService} from "../../../components/shared/page-width.service";

@Component({
  selector: "kpn-change-header",
  template: `
    <div *ngIf="isSmall()">
      <div class="kpn-line">
        <kpn-link-changeset
          [changeSetId]="changeKey.changeSetId"
          [replicationNumber]="changeKey.replicationNumber"
          class="kpn-thick">
        </kpn-link-changeset>
        <kpn-icon-happy *ngIf="happy"></kpn-icon-happy>
        <kpn-icon-investigate *ngIf="investigate"></kpn-icon-investigate>
      </div>
      <div>
        <kpn-timestamp [timestamp]="changeKey.timestamp" class="kpn-thin"></kpn-timestamp>
      </div>
    </div>

    <div *ngIf="!isSmall()" class="kpn-line">
      <kpn-link-changeset
        [changeSetId]="changeKey.changeSetId"
        [replicationNumber]="changeKey.replicationNumber"
        class="kpn-thick">
      </kpn-link-changeset>
      <kpn-timestamp [timestamp]="changeKey.timestamp" class="kpn-thin"></kpn-timestamp>
      <kpn-icon-happy *ngIf="happy"></kpn-icon-happy>
      <kpn-icon-investigate *ngIf="investigate"></kpn-icon-investigate>
    </div>

    <div *ngIf="comment" class="comment">
      {{comment}}
    </div>

  `,
  styles: [`
    .comment {
      padding-top: 5px;
      padding-bottom: 5px;
      font-style: italic;
    }
  `]
})
export class ChangeHeaderComponent {

  @Input() changeKey: ChangeKey;
  @Input() happy: boolean;
  @Input() investigate: boolean;
  @Input() comment: string;

  constructor(private pageWidthService: PageWidthService) {
  }

  isSmall() {
    return this.pageWidthService.isSmall();
  }

}
