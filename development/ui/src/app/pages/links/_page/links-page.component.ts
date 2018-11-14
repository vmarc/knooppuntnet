import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'kpn-links-page',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-sidenav sidenav></kpn-sidenav>
      <div content>
        <h1>
          Links
        </h1>
      </div>
    </kpn-page>
  `
})
export class LinksPageComponent implements OnInit {

  constructor() {
  }

  ngOnInit() {
  }

}
