import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangeSetDetail } from '@api/common/changes/change-set-detail';
import { Util } from '@app/components/shared';
import { OsmLinkChangeSetComponent } from '@app/components/shared/link';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { ChangeSetAnalysisComponent } from './change-set-analysis.component';

@Component({
  selector: 'kpn-change-set-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table class="kpn-table">
      <tbody>
        <tr>
          <td i18n="@@change-set.header.change-set">Changeset</td>
          <td>
            <div class="kpn-line">
              <span>{{ detail().summary.key.changeSetId }}</span>
              <kpn-osm-link-change-set [changeSetId]="detail().summary.key.changeSetId" />
              <span>
                <a
                  class="external"
                  rel="nofollow noreferrer"
                  target="_blank"
                  [href]="
                    'https://overpass-api.de/achavi/?changeset=' + detail().summary.key.changeSetId
                  "
                  i18n="@@change-set.header.achavi"
                >
                  achavi
                </a>
              </span>
              <span>
                <a
                  class="external"
                  rel="nofollow noreferrer"
                  target="_blank"
                  [href]="'https://osmcha.org/changesets/' + detail().summary.key.changeSetId"
                  i18n="@@change-set.header.osmcha"
                >
                  osmcha
                </a>
              </span>
            </div>
          </td>
        </tr>
        <tr>
          <td i18n="@@change-set.header.timestamp">Timestamp</td>
          <td>
            <kpn-timestamp [timestamp]="detail().summary.key.timestamp" />
          </td>
        </tr>
        <tr>
          <td i18n="@@change-set.header.replication-number">Minute diff</td>
          <td>
            {{ replicationName() }}
          </td>
        </tr>
        @if (hasComment()) {
          <tr>
            <td i18n="@@change-set.header.comment">Comment</td>
            <td>
              {{ comment() }}
            </td>
          </tr>
        }
        <tr>
          <td i18n="@@change-set.header.analysis">Analysis</td>
          <td>
            <kpn-change-set-analysis [detail]="detail()" />
          </td>
        </tr>
      </tbody>
    </table>
  `,
  standalone: true,
  imports: [ChangeSetAnalysisComponent, OsmLinkChangeSetComponent, TimestampComponent],
})
export class ChangeSetHeaderComponent {
  detail = input.required<ChangeSetDetail>();

  replicationName() {
    return Util.replicationName(this.detail().summary.key.replicationNumber);
  }

  hasComment() {
    return (
      this.detail().changeSetInfo &&
      this.detail().changeSetInfo.tags.filter((t) => t.key === 'comment').length > 0
    );
  }

  comment() {
    return Util.tagWithKey(this.detail().changeSetInfo.tags, 'comment');
  }
}
