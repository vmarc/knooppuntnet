import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { IconButtonComponent } from '@app/components/shared/icon';
import { OldPageComponent } from '@app/components/shared/page';
import { PageHeaderComponent } from '@app/components/shared/page';
import { BaseSidebarComponent } from '../../base-sidebar.component';

@Component({
  selector: 'kpn-home-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-old-page>
      <kpn-page-header [pageTitle]="null" subject="home" i18n="@@home.page-title"
        >Node networks
      </kpn-page-header>
      <kpn-icon-button routerLink="/map" icon="map" title="Map" i18n-title="@@home.map" />
      <kpn-icon-button
        routerLink="/analysis"
        icon="analysis"
        title="Analysis"
        i18n-title="@@home.analysis"
      />
      <kpn-icon-button
        routerLink="/monitor"
        icon="monitor"
        title="Monitor"
        i18n-title="@@home.monitor"
      />
      <kpn-base-sidebar sidebar />
    </kpn-old-page>
  `,
  standalone: true,
  imports: [
    BaseSidebarComponent,
    IconButtonComponent,
    OldPageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class HomePageComponent {}
