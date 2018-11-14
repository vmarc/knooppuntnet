import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'kpn-glossary-page',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <kpn-sidenav sidenav></kpn-sidenav>
      <div content>
        <h1>
          Glossary
        </h1>
      </div>
    </kpn-page>
  `
})
export class GlossaryPageComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
