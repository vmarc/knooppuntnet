import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-fact-route-tag-invalid',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown ngPreserveWhitespaces i18n="@@fact.description.route-tag-invalid">
      Invalid value in required tag *"route"* in route relation. A bicycle route relation needs to
      have value *"bicycle"* in its *"route"* tag. A hiking route relation needs to have one of the
      following values in its *"route"* tag: *"foot"*, *"hiking"*, or *"walking"*. Note that
      *"walking"* is a value that is frequently found, but not actually documented as a valid value
      in the OSM wiki pages. Other potential values are *"horse"*, *"motorboat"*, *"canoe"* and
      *"inline_skates"*.
    </markdown>
  `,
  standalone: true,
  imports: [MarkdownModule],
})
export class FactRouteTagInvalidComponent {}
