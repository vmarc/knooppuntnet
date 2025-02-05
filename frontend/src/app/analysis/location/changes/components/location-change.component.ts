import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LocationChangeSetInfo } from '@api/common/location-change-set-info';
import { ChangeHeaderComponent } from '@app/analysis/components/change-set';
import { ChangesSetElementRefsComponent } from '@app/analysis/components/change-set/components';
import { LocationPipe } from '../../../../shared/components/format/location.pipe';

@Component({
  selector: 'kpn-location-change',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="change-set">
      <kpn-change-header
        [changeKey]="changeSet().key"
        [happy]="changeSet().happy"
        [investigate]="changeSet().investigate"
        [comment]="changeSet().comment"
      />

      @for (locationChanges of changeSet().locationChanges; track locationChanges) {
        <div>
          <div class="kpn-line">
            <div class="location-names">
              @for (
                locationInfo of locationChanges.locationInfos;
                track locationInfo;
                let i = $index
              ) {
                <div class="location-name">
                  <a [routerLink]="locationLink(locationInfo.link)">{{
                    locationInfo.name | location
                  }}</a>
                </div>
              }
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
      }
    </div>
  `,
  styles: `
    .change-set {
      margin-top: 0.5em;
      margin-bottom: 0.5em;
    }

    .location-names {
      display: inline;
      padding-top: 0.6em;
      padding-bottom: 0.3em;
    }

    .location-name {
      display: inline;
    }

    .location-names :not(:last-child):after {
      content: ' \\2192 \\0020 ';
    }
  `,
  imports: [ChangeHeaderComponent, ChangesSetElementRefsComponent, RouterLink, LocationPipe],
})
export class LocationChangeComponent {
  changeSet = input.required<LocationChangeSetInfo>();

  locationLink(link: string): string {
    return `/analysis/${link}/details`;
  }
}
