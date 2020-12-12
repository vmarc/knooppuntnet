import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {PageWidthService} from '../../components/shared/page-width.service';
import {LongDistanceRouteChangeSummary} from '../../kpn/api/common/longdistance/long-distance-route-change-summary';

@Component({
  selector: 'kpn-long-distance-route-change-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <a [routerLink]="link()" class="kpn-thick">{{changeSet.key.changeSetId}}</a>
      <span *ngIf="timestampOnSameLine$ | async" class="kpn-thin">{{changeSet.key.timestamp}}</span>
      <kpn-icon-happy *ngIf="changeSet.happy"></kpn-icon-happy>
      <kpn-icon-investigate *ngIf="changeSet.investigate"></kpn-icon-investigate>
    </div>
    <div *ngIf="timestampOnSeparateLine$ | async">
      <span class="kpn-thin">{{changeSet.key.timestamp}}</span>
    </div>

    <div *ngIf="changeSet.comment" class="comment">
      {{changeSet.comment}}
    </div>
  `,
  styles: [`
    .comment {
      padding-top: 5px;
      padding-bottom: 5px;
      font-style: italic;
    }
  `]
})
export class LongDistanceRouteChangeHeaderComponent {

  @Input() changeSet: LongDistanceRouteChangeSummary;

  timestampOnSameLine$: Observable<boolean>;
  timestampOnSeparateLine$: Observable<boolean>;

  constructor(private pageWidthService: PageWidthService) {
    this.timestampOnSeparateLine$ = this.pageWidthService.current$.pipe(map(() => this.timestampOnSeparateLine()));
    this.timestampOnSameLine$ = this.timestampOnSeparateLine$.pipe(map(value => !value));
  }

  private timestampOnSeparateLine() {
    return this.pageWidthService.isSmall() || this.pageWidthService.isVerySmall() || this.pageWidthService.isVeryVerySmall();
  }

  link(): string {
    return `/long-distance/routes/${this.changeSet.key.elementId}/changes/${this.changeSet.key.changeSetId}`;
  }

}
