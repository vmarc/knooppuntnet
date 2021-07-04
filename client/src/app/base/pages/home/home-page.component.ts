import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { PageService } from '../../../components/shared/page.service';

@Component({
  selector: 'kpn-home-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-header [pageTitle]="null" subject="home" i18n="@@home.page-title"
      >Node networks</kpn-page-header
    >
    <kpn-icon-button routerLink="/map" icon="map" i18n="@@home.map"
      >Map</kpn-icon-button
    >
    <kpn-icon-button
      routerLink="/analysis"
      icon="analysis"
      i18n="@@home.analysis"
      >Analysis</kpn-icon-button
    >
    <kpn-icon-button routerLink="/demo" icon="video" i18n="@@home.demo"
      >Demo</kpn-icon-button
    >
  `,
})
export class HomePageComponent implements OnInit {
  constructor(private pageService: PageService) {}

  ngOnInit(): void {
    this.pageService.defaultMenu();
  }
}
