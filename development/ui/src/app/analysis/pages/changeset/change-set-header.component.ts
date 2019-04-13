import {Component, Input} from "@angular/core";
import {Util} from "../../../components/shared/util";
import {ChangeSetPage} from "../../../kpn/shared/changes/change-set-page";

@Component({
  selector: "kpn-change-set-header",
  template: `
    <table class="kpn-table">
      <tbody>
      <tr>
        <td>
          Changeset <!-- Wijzigingenset -->
        </td>
        <td>
          <div class="kpn-line">
            <span>{{page.summary.key.changeSetId}}</span>
            <osm-link-change-set [id]="page.summary.key.changeSetId"></osm-link-change-set>
            <a
              class="external"
              rel="nofollow"
              target="_blank"
              [href]="'https://overpass-api.de/achavi/?changeset=' + page.summary.key.changeSetId">
              achavi
            </a>
          </div>
        </td>
      </tr>
      <tr>
        <td>
          Minute diff 
          <!-- Replicatie nummer -->
        </td>
        <td>
          {{replicationName()}}
        </td>
      </tr>
      <tr *ngIf="hasComment()">
        <td>
          Comment 
          <!-- Commentaar -->
        </td>
        <td>
          {{comment()}}
        </td>
      </tr>
      <tr>
        <td>
          Analysis 
          <!-- Analyse -->
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
