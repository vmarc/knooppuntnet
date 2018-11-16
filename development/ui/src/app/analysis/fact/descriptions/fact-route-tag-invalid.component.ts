import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-tag-invalid',
  template: `
    <!--
    |De verplichte _"route"_ tag heeft een ongeldige waarde.
    |
    |Een fietsnetwerk route relatie moet minimaal een tag met sleutel _"route"_ en waarde _"bicycle"_ hebben.
    |
    |Een wandelnetwerk route relatie moet minimaal een tag met sleutel _"route"_ en waarde _"foot"_,
    | _"hiking"_ of _"walking"_ hebben. De waarde _"walking"_ wordt ook vaak gevonden en door de analyse
    | logica aanvaard, al is deze waarde niet gedocumenteerd in de OSM wiki paginas.
    -->

    <markdown ngPreserveWhitespaces>
      Invalid value in required tag _"route"_ in route relation.

      A bicycle route relation needs to have value _"bicycle"_ in its _"route"_ tag.

      A hiking route relation needs to have one of the following values in its _"route"_ tag: _"foot"_,
      _"hiking"_, or _"walking"_. Note that _"walking"_ is a value that is frequently found,
      but not actually documented as a valid value in the OSM wiki pages.
    </markdown>
  `
})
export class FactRouteTagInvalidComponent {
}
