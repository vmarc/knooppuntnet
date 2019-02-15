import {Component, OnInit} from '@angular/core';
import {PageService} from "../../../components/shared/page.service";

@Component({
  selector: 'kpn-about-page',
  template: `
    <h1>
      About
    </h1>

    <p>Point of interest icons are by <a href="https://mapicons.mapsmarker.com/" class="external" target="_blank">Map Icons</a>.</p>

    <p>Other icons are from <a href="https://www.flaticon.com/" title="Flaticon" class="external" target="_blank">www.flaticon.com</a>
      and are licensed by
      <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" class="external" target="_blank">CC 3.0 BY</a>.
      These icons are made by:
    </p>

    <li><a href="https://www.flaticon.com/authors/scott-de-jonge" title="Scott de Jonge" class="external" target="_blank">Scott de Jonge</a></li>
    <li><a href="https://www.flaticon.com/authors/vitaly-gorbachev" title="Vitaly Gorbachev" class="external" target="_blank">Vitaly Gorbachev</a></li>
    <li><a href="https://www.freepik.com/" title="Freepik" class="external" target="_blank">Freepik</a></li>
    <li><a href="https://www.flaticon.com/authors/plainicon" title="Plainicon" class="external" target="_blank">Plainicon</a></li>
    <li><a href="https://www.flaticon.com/authors/google" title="Google" class="external" target="_blank">Google</a></li>
    <li><a href="https://www.flaticon.com/authors/smashicons" title="Smashicons" class="external" target="_blank">Smashicons</a></li>
    <li><a href="https://www.flaticon.com/authors/dmitri13" title="dmitri13" class="external" target="_blank">dmitri13</a></li>
    <li><a href="https://www.flaticon.com/authors/roundicons" title="Roundicons" class="external" target="_blank">Roundicons</a></li>
  `
})
export class AboutPageComponent implements OnInit {

  constructor(private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
  }

}
