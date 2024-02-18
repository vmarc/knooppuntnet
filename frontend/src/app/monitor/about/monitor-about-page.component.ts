import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PageHeaderComponent } from '@app/components/shared/page';
import { PageComponent } from '@app/components/shared/page';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorPageMenuComponent } from '../components/monitor-page-menu.component';

@Component({
  selector: 'kpn-monitor-about',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/monitor" i18n="@@breadcrumb.monitor">Monitor</a>
        </li>
        <li i18n="@@breadcrumb.monitor.about">About</li>
      </ul>

      <kpn-page-header>
        <ng-container i18n="@@monitor.about.title">Monitor</ng-container>
      </kpn-page-header>

      <kpn-monitor-page-menu pageName="about" />

      <div class="kpn-comment kpn-spacer-above">
        <p i18n="@@monitor.about.text">
          The route monitoring function allows you to compare a route geometry in OpenStreetMap
          against a given reference geometry.
        </p>
      </div>
      <kpn-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    MonitorPageMenuComponent,
    PageComponent,
    RouterLink,
    SidebarComponent,
    PageHeaderComponent,
  ],
})
export class MonitorAboutPageComponent {}
