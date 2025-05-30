import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzDropDownDirective } from 'ng-zorro-antd/dropdown';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzMenuItemComponent } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { toLonLat } from 'ol/proj';
import { OpenlayersMapService } from '../services/openlayers-map-service';
import { MAP_SERVICE_TOKEN } from '../services/openlayers-map-service';

@Component({
  selector: 'ui-map-link-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <a nz-dropdown [nzDropdownMenu]="linkMenu">
        <span>Link</span>
        <nz-icon nzType="down" />
      </a>
      <nz-dropdown-menu #linkMenu="nzDropdownMenu">
        <ul nz-menu nzSelectable>
          <li nz-menu-item (click)="goto('iD')">iD</li>
          <li nz-menu-item (click)="goto('openstreetmap')">OpenStreetMap</li>
          <li nz-menu-item (click)="goto('mapillary')">Mapillary</li>
          <li nz-menu-item (click)="goto('google')">Google</li>
          <li nz-menu-item (click)="goto('google-satellite')">Google Satellite</li>
        </ul>
      </nz-dropdown-menu>
    </div>
  `,
  imports: [
    NzDropDownDirective,
    NzDropdownMenuComponent,
    NzIconDirective,
    NzMenuDirective,
    NzMenuItemComponent,
  ],
})
export class MapLinkMenuComponent {
  private readonly openlayersMapService: OpenlayersMapService = inject(MAP_SERVICE_TOKEN);

  goto(target: string): void {
    const zoom = Math.round(this.openlayersMapService.map.getView().getZoom());
    const center = toLonLat(this.openlayersMapService.map.getView().getCenter());
    let url = '';
    if (target === 'openstreetmap') {
      url = `https://www.openstreetmap.org/#map=${zoom}/${center[1]}/${center[0]}`;
    } else if (target === 'mapillary') {
      url = `https://www.mapillary.com/app/?lat=${center[1]}&lng=${center[0]}&z=${zoom}`;
    } else if (target === 'google') {
      url = `https://www.google.com/maps/@?api=1&map_action=map&center=${center[1]},${center[0]}&zoom=${zoom}`;
    } else if (target === 'google-satellite') {
      url = `https://www.google.com/maps/@?api=1&map_action=map&center=${center[1]},${center[0]}&zoom=${zoom}&basemap=satellite`;
    } else if (target === 'iD') {
      url = `https://www.openstreetmap.org/edit?editor=id#map=${zoom}/${center[1]}/${center[0]}`;
    }
    window.open(encodeURI(url), '_blank');
  }
}
