import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { PoiAnalysis } from '@api/common';
import { PoiService } from '@app/services';

@Component({
  selector: 'kpn-poi-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (poi().name) {
      <h2>{{ poi().name }}</h2>
    } @else {
      <h2>{{ layerName() }}</h2>
    }

    @if (poi().name) {
      <div class="item">{{ layerName() }}</div>
    }

    @if (poi().subject) {
      <div class="item">{{ poi().subject }}</div>
    }
    @if (poi().denomination) {
      <div class="item">{{ poi().denomination }}</div>
    }
    @if (poi().cuisine) {
      <div class="item">{{ poi().cuisine }}</div>
    }

    @if (poi().addressLine1 || poi().addressLine2) {
      <div class="item">
        @if (poi().addressLine1) {
          <span>{{ poi().addressLine1 }}</span>
        }
        <br />
        @if (poi().addressLine2) {
          <span>{{ poi().addressLine2 }}</span>
        }
      </div>
    }

    @if (poi().phone) {
      <div class="item">
        <span class="kpn-label" i18n="@@poi.detail.phone">Phone</span>
        {{ poi().phone }}
      </div>
    }

    @if (poi().fax) {
      <div class="item">
        <span class="kpn-label" i18n="@@poi.detail.fax">Fax</span>
        {{ poi().fax }}
      </div>
    }

    @if (poi().email) {
      <div class="item">
        <span class="kpn-label" i18n="@@poi.detail.email">E-mail</span>
        <a [href]="emailLink()">{{ poi().email }}</a>
      </div>
    }

    @if (poi().facebook || poi().twitter) {
      <div class="item">
        @if (poi().facebook) {
          <a [href]="poi().facebook" target="_blank" rel="nofollow noreferrer">
            <img
              src="/assets/images/icons/facebook.png"
              class="image"
              alt="Facebook"
              title="Facebook"
            />
          </a>
        }
        @if (poi().twitter) {
          <a [href]="poi().twitter" target="_blank" rel="nofollow noreferrer">
            <img
              src="/assets/images/icons/twitter.png"
              class="image"
              alt="Twitter"
              title="Twitter"
            />
          </a>
        }
      </div>
    }

    @if (poi().description) {
      <div class="item">{{ poi().description }}</div>
    }
    @if (poi().wheelchair) {
      <div class="item">
        <span class="kpn-label" i18n="@@poi.detail.wheelchair">Wheelchair</span>
        {{ poi().wheelchair }}
      </div>
    }

    @if (poi().openingHours) {
      <div class="item">
        <span class="kpn-label" i18n="@@poi.detail.opengingHours">Opening hours</span>
        {{ poi().openingHours }}
      </div>
    }
    @if (poi().serviceTimes) {
      <div class="item">
        <span class="kpn-label" i18n="@@poi.detail.serviceHours">Service times</span>
        {{ poi().serviceTimes }}
      </div>
    }

    @if (poi().image) {
      <div class="item">
        <a [href]="poi().image" target="_blank" rel="nofollow noreferrer">
          <img
            [src]="poi().imageThumbnail"
            width="inherit"
            height="100px"
            alt="image"
            class="image"
          />
        </a>
      </div>
    }

    @if (poi().imageLink) {
      <div class="item">
        <a
          [href]="poi().imageLink"
          class="external"
          target="_blank"
          rel="nofollow noreferrer"
          i18n="@@poi.detail.image"
        >
          Image
        </a>
      </div>
    }

    @if (poi().mapillary) {
      <div class="item">
        <a
          [href]="poi().mapillary"
          class="external"
          target="_blank"
          rel="nofollow noreferrer"
          i18n="@@poi.detail.mapillary"
        >
          Mapillary
        </a>
      </div>
    }

    @if (poi().onroerendErfgoed) {
      <div class="item">
        <a
          [href]="poi().onroerendErfgoed"
          i18n="@@poi.detail.onroerendErfgoed"
          class="external"
          target="_blank"
          rel="nofollow noreferrer"
        >
          Onroerend Erfgoed
        </a>
      </div>
    }

    @if (poi().website || poi().wikidata || poi().wikipedia) {
      <div class="item">
        @if (poi().website) {
          <a
            [href]="poi().website"
            class="external"
            target="_blank"
            rel="nofollow noreferrer"
            i18n="@@poi.detail.website"
          >
            Website
          </a>
        }
        @if (poi().wikidata) {
          <a
            [href]="poi().wikidata"
            class="external"
            target="_blank"
            rel="nofollow noreferrer"
            i18n="@@poi.detail.wikidata"
          >
            Wikidata
          </a>
        }
        @if (poi().wikipedia) {
          <a
            [href]="poi().wikipedia"
            class="external"
            target="_blank"
            rel="nofollow noreferrer"
            i18n="@@poi.detail.wikipedia"
          >
            Wikipedia
          </a>
        }
      </div>
    }

    @if (poi().molenDatabase) {
      <div class="item">
        <a
          [href]="poi().molenDatabase"
          class="external"
          target="_blank"
          rel="nofollow noreferrer"
          i18n="@@poi.detail.molen-database"
        >
          Molen database
        </a>
      </div>
    }

    @if (poi().hollandscheMolenDatabase) {
      <div class="item">
        <a
          [href]="poi().hollandscheMolenDatabase"
          class="external"
          target="_blank"
          rel="nofollow noreferrer"
          i18n="@@poi.detail.hollandsche-molen-database"
        >
          Hollandsche Molen database
        </a>
      </div>
    }
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
  imports: [],
})
export class PoiAnalysisComponent {
  poi = input.required<PoiAnalysis>();

  private readonly poiService = inject(PoiService);

  layerName(): string {
    const layer = this.poi().layers[0];
    return this.poiService.name(layer);
  }

  emailLink(): string {
    return 'mailto:' + this.poi().email;
  }
}
