import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MatInput } from '@angular/material/input';
import { MatNavList } from '@angular/material/list';
import { MatListItem } from '@angular/material/list';
import { MatFormField } from '@angular/material/select';
import { MatLabel } from '@angular/material/select';
import { MatSlideToggle } from '@angular/material/slide-toggle';
import { GeocoderLocation } from '@api/common/geocoder-location';
import { DividerComponent } from '@app/components/shared';
import { BackButtonComponent } from '@app/components/shared';
import { PageButtonsComponent } from '@app/components/shared/page';
import { ApiService } from '@app/services';
import { ReactiveFormsModule } from '@angular/forms';
import { PageComponent } from '../shared/components/shared/page/page.component';
import { SearchComponent } from './advanced/search.component';
import { ExploreLayersComponent } from './explore-layers.component';
import { ExploreModeComponent } from './explore-mode.component';
import { ExplorePoiComponent } from './explore-poi.component';
import { ExploreRoutesComponent } from './explore-routes.component';
import { ExploreScopeComponent } from './explore-scope.component';

@Component({
  selector: 'kpn-explore',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-buttons>
      <kpn-back-button />
      <mat-label>Explore</mat-label>
    </kpn-page-buttons>
    <kpn-page>
      <form [formGroup]="form" class="kpn-form" #ngForm="ngForm">
        <mat-form-field appearance="outline">
          <mat-label>Search</mat-label>
          <input matInput placeholder="location, relation id" [formControl]="query" />
        </mat-form-field>
        <div>
          <button mat-stroked-button (click)="search()">Search</button>
        </div>
      </form>
      <mat-slide-toggle class="kpn-small-spacer-above">Advanced</mat-slide-toggle>

      <kpn-divider class="kpn-spacer-above" />
      <kpn-search />
      <kpn-divider />

      @if (geocoderLocations().length > 0) {
        <kpn-divider />
        <p>Locations</p>
        <mat-nav-list>
          @for (location of geocoderLocations(); track location.name) {
            <mat-list-item>
              {{ location.name }}
            </mat-list-item>
          }
        </mat-nav-list>
        <kpn-divider />
      }
      <kpn-explore-routes />
      <kpn-explore-mode />
      <kpn-explore-scope />
      <kpn-explore-layers />
      <kpn-explore-poi />
    </kpn-page>
  `,
  imports: [
    BackButtonComponent,
    MatLabel,
    PageButtonsComponent,
    MatFormField,
    MatInput,
    PageComponent,
    MatButton,
    FormsModule,
    ReactiveFormsModule,
    DividerComponent,
    MatListItem,
    MatNavList,
    PageComponent,
    ExploreRoutesComponent,
    ExploreScopeComponent,
    ExploreModeComponent,
    ExplorePoiComponent,
    ExploreLayersComponent,
    SearchComponent,
    MatSlideToggle,
  ],
})
export class ExploreComponent {
  private readonly apiService = inject(ApiService);
  readonly geocoderLocations = signal<GeocoderLocation[]>([]);
  readonly query = new FormControl<string>('');

  readonly form = new FormGroup({
    query: this.query,
  });

  search(): void {
    this.apiService.search(this.query.value).subscribe((response) => {
      this.geocoderLocations.set(response.result.geocoderLocations);
    });
  }
}
