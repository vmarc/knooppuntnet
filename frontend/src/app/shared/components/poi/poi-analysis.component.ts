import { NgIf } from '@angular/common';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiAnalysis } from '@api/common';
import { Tags } from '@api/custom';
import { PoiService } from '@app/services';

@Component({
  selector: 'kpn-poi-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <h2 *ngIf="poi.name">{{ poi.name }}</h2>
    <h2 *ngIf="!poi.name">{{ layerName() }}</h2>

    <div *ngIf="poi.name" class="item">{{ layerName() }}</div>

    <div *ngIf="poi.subject" class="item">{{ poi.subject }}</div>
    <div *ngIf="poi.denomination" class="item">{{ poi.denomination }}</div>
    <div *ngIf="poi.cuisine" class="item">{{ poi.cuisine }}</div>

    <div *ngIf="poi.addressLine1 || poi.addressLine2" class="item">
      <span *ngIf="poi.addressLine1">{{ poi.addressLine1 }}</span
      ><br />
      <span *ngIf="poi.addressLine2">{{ poi.addressLine2 }}</span>
    </div>

    <div *ngIf="poi.phone" class="item">
      <span class="kpn-label" i18n="@@poi.detail.phone">Phone</span>
      {{ poi.phone }}
    </div>

    <div *ngIf="poi.fax" class="item">
      <span class="kpn-label" i18n="@@poi.detail.fax">Fax</span>
      {{ poi.fax }}
    </div>

    <div *ngIf="poi.email" class="item">
      <span class="kpn-label" i18n="@@poi.detail.email">E-mail</span>
      <a [href]="emailLink()">{{ poi.email }}</a>
    </div>

    <div *ngIf="poi.facebook || poi.twitter" class="item">
      <a
        *ngIf="poi.facebook"
        [href]="poi.facebook"
        target="_blank"
        rel="nofollow noreferrer"
      >
        <img
          src="/assets/images/icons/facebook.png"
          class="image"
          alt="Facebook"
          title="Facebook"
        />
      </a>
      <a
        *ngIf="poi.twitter"
        [href]="poi.twitter"
        target="_blank"
        rel="nofollow noreferrer"
      >
        <img
          src="/assets/images/icons/twitter.png"
          class="image"
          alt="Twitter"
          title="Twitter"
        />
      </a>
    </div>

    <div *ngIf="poi.description" class="item">{{ poi.description }}</div>
    <div *ngIf="poi.wheelchair" class="item">
      <span class="kpn-label" i18n="@@poi.detail.wheelchair">Wheelchair</span>
      {{ poi.wheelchair }}
    </div>

    <div *ngIf="poi.openingHours" class="item">
      <span class="kpn-label" i18n="@@poi.detail.opengingHours"
        >Opening hours</span
      >
      {{ poi.openingHours }}
    </div>
    <div *ngIf="poi.serviceTimes" class="item">
      <span class="kpn-label" i18n="@@poi.detail.serviceHours"
        >Service times</span
      >
      {{ poi.serviceTimes }}
    </div>

    <div *ngIf="poi.image" class="item">
      <a [href]="poi.image" target="_blank" rel="nofollow noreferrer">
        <img
          [src]="poi.imageThumbnail"
          width="inherit"
          height="100px"
          alt="image"
          class="image"
        />
      </a>
    </div>

    <div *ngIf="poi.imageLink" class="item">
      <a
        [href]="poi.imageLink"
        class="external"
        target="_blank"
        rel="nofollow noreferrer"
        i18n="@@poi.detail.image"
      >
        Image
      </a>
    </div>

    <div *ngIf="poi.mapillary" class="item">
      <a
        [href]="poi.mapillary"
        class="external"
        target="_blank"
        rel="nofollow noreferrer"
        i18n="@@poi.detail.mapillary"
      >
        Mapillary
      </a>
    </div>

    <div *ngIf="poi.onroerendErfgoed" class="item">
      <a
        [href]="poi.onroerendErfgoed"
        i18n="@@poi.detail.onroerendErfgoed"
        class="external"
        target="_blank"
        rel="nofollow noreferrer"
      >
        Onroerend Erfgoed
      </a>
    </div>

    <div *ngIf="poi.website || poi.wikidata || poi.wikipedia" class="item">
      <a
        *ngIf="poi.website"
        [href]="poi.website"
        class="external"
        target="_blank"
        rel="nofollow noreferrer"
        i18n="@@poi.detail.website"
      >
        Website
      </a>
      <a
        *ngIf="poi.wikidata"
        [href]="poi.wikidata"
        class="external"
        target="_blank"
        rel="nofollow noreferrer"
        i18n="@@poi.detail.wikidata"
      >
        Wikidata
      </a>
      <a
        *ngIf="poi.wikipedia"
        [href]="poi.wikipedia"
        class="external"
        target="_blank"
        rel="nofollow noreferrer"
        i18n="@@poi.detail.wikipedia"
      >
        Wikipedia
      </a>
    </div>

    <div *ngIf="poi.molenDatabase" class="item">
      <a
        [href]="poi.molenDatabase"
        class="external"
        target="_blank"
        rel="nofollow noreferrer"
        i18n="@@poi.detail.molen-database"
      >
        Molen database
      </a>
    </div>

    <div *ngIf="poi.hollandscheMolenDatabase" class="item">
      <a
        [href]="poi.hollandscheMolenDatabase"
        class="external"
        target="_blank"
        rel="nofollow noreferrer"
        i18n="@@poi.detail.hollandsche-molen-database"
      >
        Hollandsche Molen database
      </a>
    </div>
  `,
  styles: `
    .item {
      margin-top: 10px;
      margin-bottom: 10px;
    }

    .item * {
      margin-right: 10px;
      align-items: center;
    }
  `,
  standalone: true,
  imports: [NgIf],
})
export class PoiAnalysisComponent {
  @Input({ required: true }) poi: PoiAnalysis;
  tags: Tags;

  constructor(private poiService: PoiService) {}

  layerName(): string {
    const layer = this.poi.layers[0];
    return this.poiService.name(layer);
  }

  emailLink(): string {
    return 'mailto:' + this.poi.email;
  }
}
