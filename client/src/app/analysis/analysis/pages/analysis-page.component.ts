import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-analysis-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li i18n="@@breadcrumb.analysis">Analysis</li>
    </ul>

    <kpn-page-header i18n="@@analysis-page.title">Analysis</kpn-page-header>

    <div class="buttons">
      <kpn-icon-button routerLink="/analysis/overview" icon="overview" i18n="@@analysis-page.overview">Overview</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/changes" icon="changes" i18n="@@analysis-page.changes">Changes</kpn-icon-button>
    </div>

    <div class="buttons">
      <kpn-icon-button routerLink="/analysis/cycling" icon="cycling" i18n="@@network-type.cycling">Cycling</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/hiking" icon="hiking" i18n="@@network-type.hiking">Hiking</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/horse-riding" icon="horse-riding" i18n="@@network-type.horse-riding">Horse riding</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/motorboat" icon="motorboat" i18n="@@network-type.motorboat">Motorboat</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/canoe" icon="canoe" i18n="@@network-type.canoe">Canoe</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/inline-skating" icon="inline-skating" i18n="@@network-type.inline-skating">Inline skating</kpn-icon-button>
    </div>
  `,
  styles: [`
    .buttons {
      display: flex;
      flex-wrap: wrap;
    }
  `]
})
export class AnalysisPageComponent {
}
