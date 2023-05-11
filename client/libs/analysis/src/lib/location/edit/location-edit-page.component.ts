import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { selectSharedHttpError } from '@app/core';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { actionLocationEditPageDestroy } from '../store/location.actions';
import { actionLocationEditPageInit } from '../store/location.actions';
import { selectLocationEditPage } from '../store/location.selectors';
import { LocationEditComponent } from './location-edit.component';

@Component({
  selector: 'kpn-location-edit-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-location-page-header
      pageName="edit"
      pageTitle="Load in editor"
      i18n-pageTitle="@@location-edit.title"
    />

    <kpn-error />

    <div
      *ngIf="apiResponse() as response; else analyzing"
      class="kpn-spacer-above"
    >
      <kpn-location-response [situationOnEnabled]="false" [response]="response">
        <p
          *ngIf="response.result.tooManyNodes"
          class="too-many-nodes"
          i18n="@@location-edit.too-many-nodes.1"
        >
          This location contains more than the maximum number of nodes ({{
            response.result.maxNodes
          }}) that can be loaded in the editor in one go. This limitation is to
          avoid overloading the OpenStreetMap api while loading the node and
          route details from JOSM.
        </p>
        <p
          *ngIf="response.result.tooManyNodes"
          class="too-many-nodes"
          i18n="@@location-edit.too-many-nodes.2"
        >
          Please select a location with less nodes.
        </p>
        <kpn-location-edit
          *ngIf="!response.result.tooManyNodes"
          [page]="response.result"
        />
      </kpn-location-response>
    </div>
    <ng-template #analyzing>
      <p
        *ngIf="noHttpError$ | async"
        class="analyzing"
        i18n="@@location-edit.analyzing"
      >
        Analyzing location nodes and routes, please wait...
      </p>
    </ng-template>
  `,
  styles: [
    `
      .too-many-nodes {
        max-width: 40em;
        font-style: italic;
      }

      .analyzing {
        font-style: italic;
      }
    `,
  ],
  standalone: true,
  imports: [
    LocationPageHeaderComponent,
    ErrorComponent,
    NgIf,
    LocationResponseComponent,
    LocationEditComponent,
    AsyncPipe,
  ],
})
export class LocationEditPageComponent implements OnInit, OnDestroy {
  readonly apiResponse = this.store.selectSignal(selectLocationEditPage);
  readonly noHttpError$ = this.store
    .select(selectSharedHttpError)
    .pipe(map((error) => error == null));

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionLocationEditPageInit());
  }

  ngOnDestroy(): void {
    this.store.dispatch(actionLocationEditPageDestroy());
  }
}
