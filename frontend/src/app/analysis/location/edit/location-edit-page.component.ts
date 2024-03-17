import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationSidebarComponent } from '../location-sidebar.component';
import { LocationEditComponent } from './location-edit.component';
import { LocationEditStore } from './location-edit.store';

@Component({
  selector: 'kpn-location-edit-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-location-page-header
        pageName="edit"
        pageTitle="Load in editor"
        i18n-pageTitle="@@location-edit.title"
      />

      <kpn-error />

      @if (store.response(); as response) {
        <div class="kpn-spacer-above">
          <kpn-location-response [situationOnEnabled]="false" [response]="response">
            @if (response.result.tooManyNodes) {
              <p class="too-many-nodes" i18n="@@location-edit.too-many-nodes.1">
                This location contains more than the maximum number of nodes ({{
                  response.result.maxNodes
                }}) that can be loaded in the editor in one go. This limitation is to avoid
                overloading the OpenStreetMap api while loading the node and route details from
                JOSM.
              </p>
            }
            @if (response.result.tooManyNodes) {
              <p class="too-many-nodes" i18n="@@location-edit.too-many-nodes.2">
                Please select a location with less nodes.
              </p>
            } @else {
              <kpn-location-edit [page]="response.result" />
            }
          </kpn-location-response>
        </div>
      } @else {
        <!-- TODO       @if (noHttpError$ | async) {-->
        <p class="analyzing" i18n="@@location-edit.analyzing">
          Analyzing location nodes and routes, please wait...
        </p>
        <!--        }-->
      }
      <ng-template #analyzing>
        <!-- TODO      @if (noHttpError$ | async) {-->
        <p class="analyzing" i18n="@@location-edit.analyzing">
          Analyzing location nodes and routes, please wait...
        </p>
        <!--        }-->
      </ng-template>
      <kpn-location-sidebar sidebar />
    </kpn-page>
  `,
  styles: `
    .too-many-nodes {
      max-width: 40em;
      font-style: italic;
    }

    .analyzing {
      font-style: italic;
    }
  `,
  providers: [LocationEditStore, RouterService],
  standalone: true,
  imports: [
    AsyncPipe,
    ErrorComponent,
    LocationEditComponent,
    LocationPageHeaderComponent,
    LocationResponseComponent,
    LocationSidebarComponent,
    PageComponent,
  ],
})
export class LocationEditPageComponent {
  protected readonly store = inject(LocationEditStore);
  // TODO !!!
  // private readonly store = inject(Store);
  // readonly apiResponse = this.store.selectSignal(selectLocationEditPage);
  // readonly noHttpError$ = this.store
  //   .select(selectSharedHttpError)
  //   .pipe(map((error) => error == null));
  //
  // ngOnInit(): void {
  //   this.store.dispatch(actionLocationEditPageInit());
  // }
  //
  // ngOnDestroy(): void {
  //   this.store.dispatch(actionLocationEditPageDestroy());
  // }
}
