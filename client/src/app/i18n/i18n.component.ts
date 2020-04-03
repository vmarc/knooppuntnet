import {AfterViewInit, ChangeDetectorRef, Component, ElementRef} from "@angular/core";
import {I18nService} from "./i18n.service";

@Component({
  selector: "kpn-i18n",
  template: `
    <div *ngIf="!isRegistryUpdated()">

      <span id="@@country.nl" i18n="@@country.nl">The Netherlands</span>
      <span id="@@country.be" i18n="@@country.be">Belgium</span>
      <span id="@@country.de" i18n="@@country.de">Germany</span>
      <span id="@@country.fr" i18n="@@country.fr">France</span>
      <span id="@@country.at" i18n="@@country.at">Austria</span>

      <span id="@@network-type.hiking" i18n="@@network-type.hiking">Hiking</span>
      <span id="@@network-type.cycling" i18n="@@network-type.cycling">Cycling</span>
      <span id="@@network-type.horse-riding" i18n="@@network-type.horse-riding">Horse riding</span>
      <span id="@@network-type.motorboat" i18n="@@network-type.motorboat">Motorboat</span>
      <span id="@@network-type.canoe" i18n="@@network-type.canoe">Canoe</span>
      <span id="@@network-type.inline-skating" i18n="@@network-type.inline-skating">Inline Skating</span>

      <span id="@@subset.in" i18n="@@subset.in">in</span>

      <span id="@@map.start-node" i18n="@@map.start-node">Start node</span>
      <span id="@@map.end-node" i18n="@@map.end-node">End node</span>
      <span id="@@map.start-tentacle-node" i18n="@@map.start-tentacle-node">Start tentacle node</span>
      <span id="@@map.end-tentacle-node" i18n="@@map.end-tentacle-node">End tentacle node</span>
      <span id="@@map.redundant-node" i18n="@@map.redundant-node">Redundant node</span>

      <span id="@@map.layer.forward-route" i18n="@@map.layer.forward-route">Forward route</span>
      <span id="@@map.layer.backward-route" i18n="@@map.layer.backward-route">Backward route</span>
      <span id="@@map.layer.start-tentacle" i18n="@@map.layer.start-tentacle">Start tentacle</span>
      <span id="@@map.layer.end-tentacle" i18n="@@map.layer.end-tentacle">End tentacle</span>
      <span id="@@map.layer.unused" i18n="@@map.layer.unused">Unused</span>
      <span id="@@map.layer.nodes" i18n="@@map.layer.nodes">Nodes</span>

      <span id="@@map.layer.osm" i18n="@@map.layer.osm">OpenStreetMap</span>
      <span id="@@map.layer.other-routes" i18n="@@map.layer.other-routes">Other routes</span>
      <span id="@@map.layer.node" i18n="@@map.layer.node">Node</span>
      <span id="@@map.layer.cycling" i18n="@@map.layer.cycling">Cycling</span>
      <span id="@@map.layer.hiking" i18n="@@map.layer.hiking">Hiking</span>
      <span id="@@map.layer.horse-riding" i18n="@@map.layer.horse-riding">Horse riding</span>
      <span id="@@map.layer.motorboat" i18n="@@map.layer.motorboat">Motorboat</span>
      <span id="@@map.layer.canoe" i18n="@@map.layer.canoe">Canoe</span>
      <span id="@@map.layer.inline-skating" i18n="@@map.layer.inline-skating">Inline Skating</span>

    </div>
  `,
  styles: [`
    :host {
      display: none;
    }
  `]
})
export class I18nComponent implements AfterViewInit {

  constructor(private element: ElementRef,
              private i18nService: I18nService,
              private cdr: ChangeDetectorRef) {
  }

  ngAfterViewInit(): void {
    const divElement = this.element.nativeElement.children[0];
    if (divElement != null) {
      const elements = divElement.children;
      this.i18nService.updateRegistry(elements);
      this.cdr.detectChanges();
    }
  }

  isRegistryUpdated(): boolean {
    return this.i18nService.isRegistryUpdated();
  }
}
