import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'kpn-not-found-page',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-sidenav sidenav></kpn-sidenav>
      <div content>
        <h1>
          Not found
        </h1>
      </div>
    </kpn-page>
  `
})
export class NotFoundPageComponent implements OnInit {

  constructor() {
  }

  ngOnInit() {
  }

}
