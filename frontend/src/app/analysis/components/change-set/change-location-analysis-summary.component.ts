import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ChangeSetSummaryInfo } from '@api/common/change-set-summary-info';
import { RouteType } from '@api/common/route-type';
import { Translations } from '@app/shared/i18n/translations';
import { LocationPipe } from '@app/shared/components/format/location.pipe';
import { Util } from '@app/shared/components/util';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { ChangeHeaderComponent } from './change-header.component';
import { ChangesSetElementRefsComponent } from './components/change-set-element-refs.component';

@Component({
  selector: 'ui-change-location-analysis-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="change-set">
      <ui-change-header
        [changeKey]="changeSet().key"
        [happy]="changeSet().happy"
        [investigate]="changeSet().investigate"
        [comment]="changeSet().comment"
      />

      @for (locationChanges of changeSet().location.changes; track locationChanges) {
        <div>
          <div class="kpn-line">
            <nz-icon [nzType]="locationChanges.routeType" />
            <div class="location-names">
              @for (locationName of locationChanges.locationNames; track locationName) {
                <div class="location-name">
                  <a
                    [routerLink]="
                      locationLink(locationChanges.routeType, locationChanges.locationNames, $index)
                    "
                    >{{ locationName | location }}</a
                  >
                </div>
              }
            </div>
          </div>
          <ui-change-set-element-refs
            elementType="node"
            [changeSetElementRefs]="locationChanges.nodeChanges"
          />
          <ui-change-set-element-refs
            elementType="route"
            [changeSetElementRefs]="locationChanges.routeChanges"
          />
        </div>
      }
    </div>
  `,
  styles: `
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
  imports: [
    ChangeHeaderComponent,
    ChangesSetElementRefsComponent,
    LocationPipe,
    NzIconDirective,
    RouterLink,
    LocationPipe,
  ],
})
export class ChangeLocationAnalysisSummaryComponent {
  readonly changeSet = input.required<ChangeSetSummaryInfo>();

  locationLink(routeType: RouteType, locationNames: ReadonlyArray<string>, index: number): string {
    const country = locationNames[0].toLowerCase();
    const countryName = Translations.get('country.' + Util.safeGet(() => country));
    const locationParts = [countryName].concat(locationNames.slice(1, index + 1));
    const location = locationParts.join(':');
    return `/analysis/${routeType}/${country}/${location}/details`;
  }
}
