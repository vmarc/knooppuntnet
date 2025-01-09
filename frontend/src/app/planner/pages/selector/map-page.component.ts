import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { IconButtonComponent } from '@app/components/shared/icon';
import { IconButtonsComponent } from '@app/components/shared/icon';
import { OldPageComponent } from '@app/components/shared/page';
import { PageHeaderComponent } from '@app/components/shared/page';
import { BaseSidebarComponent } from '@app/shared/base';

@Component({
  selector: 'kpn-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-old-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li i18n="@@breadcrumb.map">Map</li>
      </ul>

      <kpn-page-header subject="planner" i18n="@@planner.map">Map</kpn-page-header>
      <kpn-icon-buttons>
        <kpn-icon-button
          routerLink="/map/cycling"
          icon="cycling"
          i18n-title="@@route-type.cycling"
          title="Cycling"
        />
        <kpn-icon-button
          routerLink="/map/hiking"
          icon="hiking"
          i18n-title="@@route-type.hiking"
          title="Hiking"
        />
        <kpn-icon-button
          routerLink="/map/horse-riding"
          icon="horse-riding"
          i18n-title="@@route-type.horse-riding"
          title="Horse riding"
        />
        <kpn-icon-button
          routerLink="/map/motorboat"
          icon="motorboat"
          i18n-title="@@route-type.motorboat"
          title="Motorboat"
        />
        <kpn-icon-button
          routerLink="/map/canoe"
          icon="canoe"
          i18n-title="@@route-type.canoe"
          title="Canoe"
        />
        <kpn-icon-button
          routerLink="/map/inline-skating"
          icon="inline-skating"
          i18n-title="@@route-type.inline-skating"
          title="Inline skating"
        />
      </kpn-icon-buttons>
      <kpn-base-sidebar sidebar />
    </kpn-old-page>
  `,
  imports: [
    BaseSidebarComponent,
    IconButtonComponent,
    IconButtonsComponent,
    OldPageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class MapPageComponent {}
