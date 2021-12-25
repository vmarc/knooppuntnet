import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { ChangeSetSummaryInfo } from '@api/common/change-set-summary-info';

@Component({
  selector: 'kpn-change-location-analysis-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="change-set">
      <kpn-change-header
        [changeKey]="changeSet.key"
        [happy]="changeSet.happy"
        [investigate]="changeSet.investigate"
        [comment]="changeSet.comment"
      >
      </kpn-change-header>

      <div *ngFor="let locationChanges of changeSet.location.changes">
        <div class="kpn-line">
          <kpn-network-type-icon
            [networkType]="locationChanges.networkType"
          ></kpn-network-type-icon>
          <div class="location-names">
            <div
              *ngFor="let locationName of locationChanges.locationNames"
              class="location-name"
            >
              <span
                ><a>{{ locationName }}</a></span
              >
            </div>
          </div>
        </div>

        <kpn-change-set-element-refs
          [elementType]="'node'"
          [changeSetElementRefs]="locationChanges.nodeChanges"
        ></kpn-change-set-element-refs>
        <kpn-change-set-element-refs
          [elementType]="'route'"
          [changeSetElementRefs]="locationChanges.routeChanges"
        >
        </kpn-change-set-element-refs>
      </div>
    </div>
  `,
  styles: [
    `
      .change-set {
        margin-top: 5px;
        margin-bottom: 5px;
      }

      .location-names {
        display: inline;
      }

      .location-name {
        display: inline;
      }

      .location-names :not(:last-child):after {
        content: ' \\2192 \\0020 ';
      }
    `,
  ],
})
export class ChangeLocationAnalysisSummaryComponent {
  @Input() changeSet: ChangeSetSummaryInfo;
}
