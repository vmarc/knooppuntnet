import {ChangeDetectionStrategy, Component} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {PageWidth} from '../../components/shared/page-width';
import {PageWidthService} from '../../components/shared/page-width.service';

@Component({
  selector: 'kpn-map-toolbar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="toolbar">
      <kpn-plan-actions></kpn-plan-actions>
      <kpn-network-type-selector *ngIf="showNetworkTypeSelector$ | async"></kpn-network-type-selector>
    </div>
  `,
  styles: [`
    .toolbar {
      display: flex;
      align-items: center;
    }
  `]
})
export class MapToolbarComponent {

  showNetworkTypeSelector$: Observable<boolean>;

  constructor(private pageWidthService: PageWidthService) {
    this.showNetworkTypeSelector$ = pageWidthService.current$.pipe(map(pageWidth => pageWidth === PageWidth.veryLarge));
  }

}
