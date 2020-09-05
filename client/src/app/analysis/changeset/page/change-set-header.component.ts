import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {Util} from "../../../components/shared/util";
import {ChangeSetPage} from "../../../kpn/api/common/changes/change-set-page";

@Component({
  selector: "kpn-change-set-header",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table class="kpn-table">
      <tbody>
      <tr>
        <td i18n="@@change-set.header.change-set">
          Changeset
        </td>
        <td>
          <div class="kpn-line">
            <span>{{page.summary.key.changeSetId}}</span>
            <kpn-osm-link-change-set [changeSetId]="page.summary.key.changeSetId"></kpn-osm-link-change-set>
            <span>
            <a
              class="external"
              rel="nofollow noreferrer"
              target="_blank"
              [href]="'https://overpass-api.de/achavi/?changeset=' + page.summary.key.changeSetId"
              i18n="@@change-set.header.achavi">
              achavi
            </a>
            </span>
            <span>
            <a
              class="external"
              rel="nofollow noreferrer"
              target="_blank"
              [href]="'https://osmcha.org/changesets/' + page.summary.key.changeSetId"
              i18n="@@change-set.header.osmcha">
              osmcha
            </a>
            </span>
          </div>
        </td>
      </tr>
      <tr>
        <td i18n="@@change-set.header.timestamp">
          Timestamp
        </td>
        <td>
          <kpn-timestamp [timestamp]="page.summary.key.timestamp"></kpn-timestamp>
        </td>
      </tr>
      <tr>
        <td i18n="@@change-set.header.replication-number">
          Minute diff
        </td>
        <td>
          {{replicationName()}}
        </td>
      </tr>
      <tr *ngIf="hasComment()">
        <td i18n="@@change-set.header.comment">
          Comment
        </td>
        <td>
          {{comment()}}
        </td>
      </tr>
      <tr>
        <td i18n="@@change-set.header.analysis">
          Analysis
        </td>
        <td>
          <kpn-change-set-analysis [page]="page"></kpn-change-set-analysis>
        </td>
      </tr>
      </tbody>
    </table>
  `
})
export class ChangeSetHeaderComponent {

  @Input() page: ChangeSetPage;

  replicationName() {
    return Util.replicationName(this.page.summary.key.replicationNumber);
  }

  hasComment() {
    return this.page.changeSetInfo && this.page.changeSetInfo.tags.has("comment");
  }

  comment() {
    return this.page.changeSetInfo.tags.get("comment");
  }
}
