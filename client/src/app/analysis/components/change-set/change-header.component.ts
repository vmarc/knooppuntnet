import {Component, Input} from "@angular/core";
import {ChangeKey} from "../../../kpn/shared/changes/details/change-key";

@Component({
  selector: "kpn-change-header",
  template: `
    <div class="kpn-line">
      <kpn-link-changeset
        [changeSetId]="changeKey.changeSetId"
        [replicationNumber]="changeKey.replicationNumber"
        class="kpn-thick">
      </kpn-link-changeset>
      <kpn-timestamp [timestamp]="changeKey.timestamp" class="kpn-thin"></kpn-timestamp>
      <mat-icon svgIcon="happy" *ngIf="happy"></mat-icon>
      <mat-icon svgIcon="investigate" *ngIf="investigate"></mat-icon>
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

}
