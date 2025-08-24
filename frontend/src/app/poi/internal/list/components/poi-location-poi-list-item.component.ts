import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LocationPoiInfo } from '@api/common/poi/location-poi-info';

@Component({
  selector: 'ui-poi-location-poi-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let poi = poiInfo();
    <div class="kpn-line">
      <span>{{ poi.rowIndex + 1 }}</span>
      @for (layer of poi.layers; track layer) {
        <span>
          {{ layer }}
        </span>
      }
      <a [routerLink]="link()">{{ poi._id }}</a>
    </div>
    @if (poi.description) {
      <div class="kpn-line">
        <span i18n="@@location-pois.table.description" class="kpn-label">Description</span>
        <span>{{ poi.description }}</span>
      </div>
    }

    @if (poi.address) {
      <div class="kpn-line">
        <span i18n="@@location-pois.table.address" class="kpn-label">Address</span>
        <span>{{ poi.address }}</span>
      </div>
    }

    @if (poi.link) {
      <div class="kpn-line">
        <span i18n="@@location-pois.table.link" class="kpn-label">Link</span>
        <span>
          {{ poi.link ? 'yes' : '' }}
        </span>
      </div>
    }

    @if (poi.image) {
      <div class="kpn-line">
        <span i18n="@@location-pois.table.image" class="kpn-label">Image</span>
        <span>
          {{ poi.image ? 'yes' : '' }}
        </span>
      </div>
    }
  `,
  imports: [RouterLink],
})
export class PoiLocationPoiListItemComponent {
  readonly rowIndex = input.required<number>();
  readonly poiInfo = input.required<LocationPoiInfo>();

  protected readonly link = computed(() => {
    const poi = this.poiInfo();
    return `/poi/${poi.elementType}/${poi.elementId}`;
  });
}
