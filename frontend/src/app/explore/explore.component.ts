import { OnInit } from '@angular/core';
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
import { State } from '@app/state';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzCheckboxComponent } from 'ng-zorro-antd/checkbox';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzInputDirective } from 'ng-zorro-antd/input';
import { NzInputGroupComponent } from 'ng-zorro-antd/input';
import { NzInputGroupWhitSuffixOrPrefixDirective } from 'ng-zorro-antd/input';
import { NzSegmentedComponent } from 'ng-zorro-antd/segmented';
import { MapService } from '../map/map.service';
import { PageComponent } from '../shared/components/shared/page/page.component';
import { SearchComponent } from './advanced/search.component';
import { ExploreRoutesComponent } from './explore-routes.component';

@Component({
  selector: 'kpn-explore',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-buttons>
      <kpn-back-button />
      <mat-label>Explore</mat-label>
    </kpn-page-buttons>
    <kpn-page>
      <nz-input-group [nzSuffix]="suffixIconSearch">
        <input type="text" nz-input placeholder="input search text" />
      </nz-input-group>
      <ng-template #suffixIconSearch>
        <nz-icon nzType="search" />
      </ng-template>
      <nz-segmented
        [nzOptions]="options"
        (nzValueChange)="handleValueChange($event)"
      ></nz-segmented>
      <label nz-checkbox>Advanced</label>
      <div>
        <button nz-button nzType="primary">
          <nz-icon nzType="search" />
          Search
        </button>
      </div>
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
      <div class="kpn-spacer-above">
        <button mat-stroked-button (click)="listRoutesInMap()">list routes in map</button>
      </div>
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
    </kpn-page>
  `,
  imports: [
    BackButtonComponent,
    DividerComponent,
    ExploreRoutesComponent,
    FormsModule,
    MatButton,
    MatFormField,
    MatInput,
    MatLabel,
    MatListItem,
    MatNavList,
    MatSlideToggle,
    NzButtonComponent,
    NzCheckboxComponent,
    NzIconDirective,
    NzInputDirective,
    NzInputGroupComponent,
    NzInputGroupWhitSuffixOrPrefixDirective,
    NzSegmentedComponent,
    PageButtonsComponent,
    PageComponent,
    PageComponent,
    ReactiveFormsModule,
    SearchComponent,
  ],
})
export class ExploreComponent implements OnInit {
  private readonly apiService = inject(ApiService);
  private readonly mapService = inject(MapService);
  private readonly state = inject(State);
  readonly geocoderLocations = signal<GeocoderLocation[]>([]);
  readonly query = new FormControl<string>('');

  readonly form = new FormGroup({
    query: this.query,
  });

  readonly options = ['Map', 'Route', 'Network'];

  ngOnInit(): void {
    this.state.map.updateSubject('explore');
  }

  handleValueChange(e: string | number): void {
    console.log(e);
  }

  search(): void {
    this.apiService.search(this.query.value).subscribe((response) => {
      this.geocoderLocations.set(response.result.geocoderLocations);
    });
  }

  listRoutesInMap(): void {
    const scopeInternational = this.state.map.scopes.scopeInternational();
    const scopeNational = this.state.map.scopes.scopeNational();
    const scopeRegional = this.state.map.scopes.scopeRegional();
    const scopeLocal = this.state.map.scopes.scopeLocal();
    const scopeNodeRoutes = this.state.map.scopes.scopeNodeRoutes();

    const routes: Map<string, { name: string; scope: string }> = new Map();
    const features = this.mapService.allFeatures();
    features.forEach((feature) => {
      const properties = feature.getProperties();
      const routeId = properties['routeId'];
      const name = properties['name'];
      const scope = properties['scope'];
      if (routeId && name) {
        let show = false;
        if (scopeInternational && scope === 'international') {
          show = true;
        }
        if (scopeNational && scope === 'national') {
          show = true;
        }
        if (scopeRegional && scope === 'regional') {
          show = true;
        }
        if (scopeLocal && scope === 'local') {
          show = true;
        }
        if (scopeNodeRoutes && !scope) {
          show = true;
        }
        if (show) {
          routes.set(routeId, { name: name, scope: scope });
        }
      }
    });
    const entries = Array.from(routes.entries()).sort((one, two) =>
      one[1].name > two[1].name ? 1 : -1
    );
    console.log(`----- ${entries.length} routes -----`);
    ['international', 'national', 'regional', 'local', 'node'].forEach((scope) => {
      const scopeEntries = entries.filter(([key, value]) => {
        return value.scope === scope || (scope === 'node' && !value.scope);
      });
      if (scopeEntries.length > 0) {
        console.log(`  ${scope} (${scopeEntries.length})`);
        scopeEntries.forEach(([routeId, value]) => {
          console.log(`    ${value.name} (${routeId})`);
        });
      }
    });
    console.log(`${entries.length} routes`);
  }
}
