import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'kpn-home-page',
  template: `
    <kpn-page>

      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-sidenav sidenav></kpn-sidenav>

      <div content>

        <h1 i18n="@@home-page-title">
          Node networks
        </h1>

        <p>
          <kpn-link-map networkType="rcn" title="Cycling Map"></kpn-link-map>
        </p>
        <p>
          <kpn-link-map networkType="rwn" title="Hiking Map"></kpn-link-map>
        </p>
        <p>
          <kpn-link-map networkType="rhn" title="Horse Map"></kpn-link-map>
        </p>
        <p>
          <kpn-link-map networkType="rmn" title="Motorboat Map"></kpn-link-map>
        </p>
        <p>
          <kpn-link-map networkType="rpn" title="Canoe Map"></kpn-link-map>
        </p>
        <br/>
        <p>
          <kpn-link-changes></kpn-link-changes>
        </p>
        <p>
          <kpn-link-overview></kpn-link-overview>
        </p>
        <br/>
        <p>Analysis</p>
        <p>The Netherlands</p>
        <p>
          <kpn-link-subset-networks country="nl" networkType="rcn" title="Cycling"></kpn-link-subset-networks>
        </p>
        <p>
          <kpn-link-subset-networks country="nl" networkType="rwn" title="Hiking"></kpn-link-subset-networks>
        </p>
        <p>
          <kpn-link-subset-networks country="nl" networkType="rhn" title="Horse"></kpn-link-subset-networks>
        </p>
        <p>
          <kpn-link-subset-networks country="nl" networkType="rmn" title="Motorboat"></kpn-link-subset-networks>
        </p>
        <p>
          <kpn-link-subset-networks country="nl" networkType="rpn" title="Canoe"></kpn-link-subset-networks>
        </p>
        <p>Belgium</p>
        <p>
          <kpn-link-subset-networks country="be" networkType="rcn" title="Cycling"></kpn-link-subset-networks>
        </p>
        <p>
          <kpn-link-subset-networks country="be" networkType="rwn" title="Hiking"></kpn-link-subset-networks>
        </p>
        <p>Germany</p>
        <p>
          <kpn-link-subset-networks country="de" networkType="rcn" title="Cycling"></kpn-link-subset-networks>
        </p>
        <br/>
        <p>
          <kpn-link-changeset changeSetId="61632740" replicationNumber="3101276"></kpn-link-changeset>
        </p>
        <br/>
        <p>
          <kpn-link-node nodeId="1" title="node-1"></kpn-link-node>
        </p>
        <p>
          <kpn-link-route routeId="1" title="route-1"></kpn-link-route>
        </p>
        <br/>
        <p>
          <kpn-link-network-details networkId="3138543"></kpn-link-network-details>
        </p>
        <p>
          <kpn-link-network-facts networkId="3138543"></kpn-link-network-facts>
        </p>
        <p>
          <kpn-link-network-map networkId="3138543"></kpn-link-network-map>
        </p>
        <p>
          <kpn-link-network-nodes networkId="3138543"></kpn-link-network-nodes>
        </p>
        <p>
          <kpn-link-network-routes networkId="3138543"></kpn-link-network-routes>
        </p>
        <p>
          <kpn-link-network-changes networkId="3138543"></kpn-link-network-changes>
        </p>
        <br/>
        <p>
          <kpn-link-subset-networks country="de" networkType="rcn"></kpn-link-subset-networks>
        </p>
        <p>
          <kpn-link-subset-facts country="de" networkType="rcn"></kpn-link-subset-facts>
        </p>
        <p>
          <kpn-link-subset-orphan-nodes country="de" networkType="rcn"></kpn-link-subset-orphan-nodes>
        </p>
        <p>
          <kpn-link-subset-orphan-routes country="de" networkType="rcn"></kpn-link-subset-orphan-routes>
        </p>
        <p>
          <kpn-link-subset-facts country="de" networkType="rcn"></kpn-link-subset-facts>
        </p>
        <p>
          <kpn-link-subset-changes country="de" networkType="rcn"></kpn-link-subset-changes>
        </p>
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

        <br/>

      </div>
    </kpn-page>
  `
})
export class HomePageComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
