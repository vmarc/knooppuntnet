import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { IconResolver } from '@angular/material/icon';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';

@Injectable()
export class IconService {
  private readonly iconRegistry = inject(MatIconRegistry);
  private readonly domSanitizer = inject(DomSanitizer);

  private readonly icons = new Map([
    // application icons
    ['analysis', 'heartbeat.svg'],
    ['map', 'planner.svg'],
    ['monitor', 'monitor.svg'],
    ['changes', 'history.svg'],
    ['overview', 'spreadsheet.svg'],
    ['link', 'link.svg'],
    ['dot', 'dot.svg'],
    ['logo', 'logo.svg'],
    ['video', 'video.svg'],
    ['play', 'play.svg'],
    ['pause', 'pause.svg'],

    // action icons
    ['add', 'add.svg'],
    ['update', 'update.svg'],
    ['remove', 'remove.svg'],
    ['pencil', 'pencil.svg'],
    ['upload', 'upload.svg'],
    ['garbage', 'garbage.svg'],
    ['menu-dots', 'menu-dots.svg'],
    ['open-in-new', 'open-in-new.svg'],
    ['node', 'node.svg'],
    ['way', 'way.svg'],
    ['relation', 'relation.svg'],
    ['route', 'route.svg'],
    ['network', 'network.svg'],
    ['menu-down-arrow', 'menu-down-arrow.svg'],

    // networkType icons
    ['cycling', 'cycling.svg'],
    ['hiking', 'hiking.svg'],
    ['horse-riding', 'horse-riding.svg'],
    ['motorboat', 'boat.svg'],
    ['canoe', 'canoe-racing.svg'],
    ['inline-skating', 'roller-skate.svg'],

    // country icons
    ['belgium', 'belgium.svg'],
    ['netherlands', 'netherlands.svg'],
    ['germany', 'germany.svg'],
    ['france', 'france.svg'],
    ['austria', 'austria.svg'],
    ['spain', 'spain.svg'],
    ['denmark', 'denmark.svg'],

    // analysis result icons
    ['happy', 'happy.svg'],
    ['investigate', 'investigate.svg'],
    ['warning', 'warning.svg'],
    ['tick', 'tick.svg'],

    // menu icons
    ['menu', 'menu-button.svg'],
    ['help', 'information.svg'],
    ['expand', 'expand-arrow.svg'],
    ['collapse', 'right.svg'],
    ['back', 'left-arrow.svg'],
    ['undo', 'undo.svg'],
    ['redo', 'redo.svg'],
    ['reset', 'reset.svg'],
    ['reverse', 'reverse.svg'],
    ['output', 'output.svg'],
    ['location', 'location.svg'],
    ['layers', 'layers.svg'],
    ['external-link', 'external-link.svg'],

    // direction icons
    ['keep-left', 'keep-left.svg'],
    ['turn-sharp-left', 'turn-sharp-left.svg'],
    ['turn-left', 'turn-left.svg'],
    ['turn-slight-left', 'turn-slight-left.svg'],
    ['continue', 'continue.svg'],
    ['turn-slight-right', 'turn-slight-right.svg'],
    ['turn-right', 'turn-right.svg'],
    ['turn-sharp-right', 'turn-sharp-right.svg'],
    ['finish', 'finish.svg'],
    ['via', 'via.svg'],
    ['roundabout', 'roundabout.svg'],
    ['keep-right', 'keep-right.svg'],
    ['scissors', 'scissors.svg'],
  ]);

  constructor() {
    this.registerIcons();
  }

  registerIcons() {
    const resolver: IconResolver = (name) => {
      const svg = this.icons.get(name);
      if (svg) {
        return this.domSanitizer.bypassSecurityTrustResourceUrl(`/assets/images/icons/${svg}`);
      }
      return null;
    };
    this.iconRegistry.addSvgIconResolver(resolver);
  }
}
