import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { ChangeSetSummaryInfo } from '@api/common/change-set-summary-info';
import { NetworkType } from '@api/custom/network-type';
import { Util } from '../../../components/shared/util';
import { I18nService } from '../../../i18n/i18n.service';

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
      />

      <div *ngFor="let locationChanges of changeSet.location.changes">
        <div class="kpn-line">
          <kpn-network-type-icon [networkType]="locationChanges.networkType" />
          <div class="location-names">
            <div
              *ngFor="
                let locationName of locationChanges.locationNames;
                let i = index
              "
              class="location-name"
            >
              <a
                [routerLink]="
                  locationLink(
                    locationChanges.networkType,
                    locationChanges.locationNames,
                    i
                  )
                "
              >{{ locationName }}</a
              >
            </div>
          </div>
        </div>

        <kpn-change-set-element-refs
          [elementType]="'node'"
          [changeSetElementRefs]="locationChanges.nodeChanges"
        />
        <kpn-change-set-element-refs
          [elementType]="'route'"
          [changeSetElementRefs]="locationChanges.routeChanges"
        />
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

  constructor(private i18nService: I18nService) {}

  locationLink(
    networkType: NetworkType,
    locationNames: string[],
    index: number
  ): string {
    const country = locationNames[0].toLowerCase();
    const countryName = this.i18nService.translation(
      '@@country.' + Util.safeGet(() => country)
    );
    const locationParts = [countryName].concat(
      locationNames.slice(1, index + 1)
    );
    const location = locationParts.join(':');
    return `/analysis/${networkType}/${country}/${location}/nodes`;
  }
}
