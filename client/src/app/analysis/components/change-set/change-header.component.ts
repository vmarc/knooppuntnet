import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {PageWidthService} from "../../../components/shared/page-width.service";
import {ChangeKey} from "../../../kpn/api/common/changes/details/change-key";

@Component({
  selector: "kpn-change-header",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
        <span *ngIf="changeKey.changeSetId == 0" i18n="@@change-header.start">
          Start
        </span>
      <kpn-link-changeset
        *ngIf="changeKey.changeSetId > 0"
        [changeSetId]="changeKey.changeSetId"
        [replicationNumber]="changeKey.replicationNumber"
        class="kpn-thick">
      </kpn-link-changeset>
      <kpn-timestamp *ngIf="timestampOnSameLine$ | async" [timestamp]="changeKey.timestamp" class="kpn-thin"></kpn-timestamp>
      <kpn-icon-happy *ngIf="happy"></kpn-icon-happy>
      <kpn-icon-investigate *ngIf="investigate"></kpn-icon-investigate>
    </div>
    <div *ngIf="timestampOnSeparateLine$ | async">
      <kpn-timestamp [timestamp]="changeKey.timestamp" class="kpn-thin"></kpn-timestamp>
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

  timestampOnSameLine$: Observable<boolean>;
  timestampOnSeparateLine$: Observable<boolean>;

  constructor(private pageWidthService: PageWidthService) {
    this.timestampOnSeparateLine$ = this.pageWidthService.current$.pipe(map(() => this.timestampOnSeparateLine()));
    this.timestampOnSameLine$ = this.timestampOnSeparateLine$.pipe(map(value => !value));
  }

  private timestampOnSeparateLine() {
    return this.pageWidthService.isSmall() || this.pageWidthService.isVerySmall() || this.pageWidthService.isVeryVerySmall();
  }

}
