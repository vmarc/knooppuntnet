import {Component, OnInit} from '@angular/core';
import {PageService} from "../../../shared/page.service";

@Component({
  selector: 'kpn-home-page',
  template: `
    <h1 i18n="@@home-page-title">
      Node networks
    </h1>

    <kpn-card-map></kpn-card-map>
    <br/>
    <kpn-card-analysis></kpn-card-analysis>
    <br/>


    <br/>
    <p>
      <kpn-link-login></kpn-link-login>
    </p>
    <p>
      <kpn-link-logout></kpn-link-logout>
    </p>
    <p>
      <kpn-link-authenticate></kpn-link-authenticate>
    </p>
    <br/>
    <p>
      <kpn-link-about></kpn-link-about>
    </p>
    <p>
      <kpn-link-glossary></kpn-link-glossary>
    </p>
    <p>
      <kpn-link-links></kpn-link-links>
    </p>
    <br/>
    <p>
      <a routerLink="/not-found" i18n="@@home-page-link-not-found">Not found</a>
    </p>
    <p>
      <a routerLink="/translations/edit">Translations</a>
    </p>
  `
})
export class HomePageComponent implements OnInit {

  constructor(private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
  }

}
