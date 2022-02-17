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
      i18n-title="@@home.map"
      title="Map"
    ></kpn-icon-button>
    <kpn-icon-button
      routerLink="/analysis"
      icon="analysis"
      i18n-title="@@home.analysis"
      title="Analysis"
    >
    </kpn-icon-button>
    <kpn-icon-button
      routerLink="/demo"
      icon="video"
      i18n-title="@@home.demo"
      title="Demo"
    >
    </kpn-icon-button>
  `,
})
export class HomePageComponent {}
