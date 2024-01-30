import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangeSetPage } from '@api/common/changes';
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
              <span>{{ page().summary.key.changeSetId }}</span>
              <kpn-osm-link-change-set [changeSetId]="page().summary.key.changeSetId" />
              <span>
                <a
                  class="external"
                  rel="nofollow noreferrer"
                  target="_blank"
                  [href]="
                    'https://overpass-api.de/achavi/?changeset=' + page().summary.key.changeSetId
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
                  [href]="'https://osmcha.org/changesets/' + page().summary.key.changeSetId"
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
            <kpn-timestamp [timestamp]="page().summary.key.timestamp" />
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
            <kpn-change-set-analysis [page]="page()" />
          </td>
        </tr>
      </tbody>
    </table>
  `,
  standalone: true,
  imports: [ChangeSetAnalysisComponent, OsmLinkChangeSetComponent, TimestampComponent],
})
export class ChangeSetHeaderComponent {
  page = input.required<ChangeSetPage>();

  replicationName() {
    return Util.replicationName(this.page().summary.key.replicationNumber);
  }

  hasComment() {
    return (
      this.page().changeSetInfo &&
      this.page().changeSetInfo.tags.tags.filter((t) => t.key === 'comment').length > 0
    );
  }

  comment() {
    return Util.tagWithKey(this.page().changeSetInfo.tags, 'comment');
  }
}
