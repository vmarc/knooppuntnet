import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-home-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-header [pageTitle]="null" subject="home" i18n="@@home.page-title"
      >Node networks
    </kpn-page-header>
    <kpn-icon-button
      routerLink="/map"
      icon="map"
      title="Map"
      i18n-title="@@home.map"
    ></kpn-icon-button>
    <kpn-icon-button
      routerLink="/analysis"
      icon="analysis"
      title="Analysis"
      i18n-title="@@home.analysis"
    >
    </kpn-icon-button>
    <kpn-icon-button
      routerLink="/monitor"
      icon="monitor"
      title="Monitor"
      i18n-title="@@home.monitor"
    >
    </kpn-icon-button>
    <kpn-icon-button
      routerLink="/demo"
      icon="video"
      title="Demo"
      i18n-title="@@home.demo"
    >
    </kpn-icon-button>
  `,
})
export class HomePageComponent {}
