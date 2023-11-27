import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { IconButtonComponent } from '@app/components/shared/icon';
import { PageComponent } from '@app/components/shared/page';
import { PageHeaderComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { BaseSidebarComponent } from '../../base-sidebar.component';

@Component({
  selector: 'kpn-home-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
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
      <kpn-icon-button routerLink="/demo" icon="video" title="Demo" i18n-title="@@home.demo" />
      <kpn-base-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    BaseSidebarComponent,
    IconButtonComponent,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
    SidebarComponent,
  ],
})
export class HomePageComponent {}
