import { computed } from '@angular/core';
import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { SharedStateService } from '@app/shared/core/shared/shared-state.service';
import { RouterService } from '@app/shared/services/router.service';
import { LocationPageHeaderComponent } from '../components/location-page-header.component';
import { LocationResponseComponent } from '../components/location-response.component';
import { LocationEditComponent } from './components/location-edit.component';
import { LocationEditPageService } from './location-edit-page.service';

@Component({
  selector: 'ui-location-edit-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-location-page-header
        pageName="edit"
        pageTitle="Load in editor"
        i18n-pageTitle="@@location-edit.title"
      />

      <ui-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          <ui-location-response [situationOnEnabled]="false" [response]="response">
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
              <ui-location-edit [page]="response.result" />
            }
          </ui-location-response>
        </div>
      } @else {
        @if (noHttpError()) {
          <p class="analyzing kpn-spacer-above" i18n="@@location-edit.analyzing">
            Analyzing location nodes and routes, please wait...
          </p>
        }
      }
    </ui-page>
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
  providers: [LocationEditPageService, RouterService],
  imports: [
    ErrorComponent,
    LocationEditComponent,
    LocationPageHeaderComponent,
    LocationResponseComponent,
    PageComponent,
  ],
})
export class LocationEditPageComponent implements OnInit {
  protected readonly service = inject(LocationEditPageService);
  private readonly sharedStateService = inject(SharedStateService);
  protected readonly noHttpError = computed(() => !this.sharedStateService.httpError());

  ngOnInit(): void {
    this.service.onInit();
  }
}
