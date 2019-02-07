import {Component, OnInit} from '@angular/core';
import {PageService} from "../../../components/shared/page.service";

@Component({
  selector: 'kpn-about-page',
  template: `
    <h1>
      About
    </h1>

    <div>Point of interest icons are by <a hef="https://mapicons.mapsmarker.com/">Map Icons</a>.</div>

    <div>Other icons made by
      <a href="https://www.flaticon.com/authors/scott-de-jonge" title="Scott de Jonge">Scott de Jonge</a>,
      <a href="https://www.flaticon.com/authors/vitaly-gorbachev" title="Vitaly Gorbachev">Vitaly Gorbachev</a>
      <a href="https://www.freepik.com/" title="Freepik">Freepik</a>,
      <a href="https://www.flaticon.com/authors/plainicon" title="Plainicon">Plainicon</a>,
      <a href="https://www.flaticon.com/authors/google" title="Google">Google</a>,
      <a href="https://www.flaticon.com/authors/smashicons" title="Smashicons">Smashicons</a>,
      <a href="https://www.flaticon.com/authors/dmitri13" title="dmitri13">dmitri13</a>
      and
      <a href="https://www.flaticon.com/authors/roundicons" title="Roundicons">Roundicons</a>
      from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a>
      are licensed by
      <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a>.
    </div>
  `
})
export class AboutPageComponent implements OnInit {

  constructor(private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
  }

}
