import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { IconResolver } from '@angular/material/icon';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';

@Injectable()
export class IconService {
  private readonly iconRegistry = inject(MatIconRegistry);
  private readonly domSanitizer = inject(DomSanitizer);

  constructor() {
    this.registerIcons();
  }

  registerIcons() {
    const resolver: IconResolver = (name) =>
      this.domSanitizer.bypassSecurityTrustResourceUrl(`/assets/images/icons/${name}.svg`);
    this.iconRegistry.setDefaultFontSetClass('material-symbols-outlined');
    this.iconRegistry.addSvgIconResolver(resolver);
  }
}
