import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'kpn-about-page',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-sidenav sidenav></kpn-sidenav>
      <div content>
        <h1>
          About
        </h1>
      </div>
    </kpn-page>
  `
})
export class AboutPageComponent implements OnInit {

  constructor() {
  }

  ngOnInit() {
  }

}
