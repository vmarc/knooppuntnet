import { OnInit } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { GeocoderLocation } from '@api/common/geocoder-location';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { ApiService } from '@app/shared/services/api.service';
import { ReactiveFormsModule } from '@angular/forms';
import { DividerComponent } from '@app/shared/components/divider.component';
import { State } from '@app/state/state';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzCheckboxComponent } from 'ng-zorro-antd/checkbox';
import { NzFormLabelComponent } from 'ng-zorro-antd/form';
import { NzFormItemComponent } from 'ng-zorro-antd/form';
import { NzFormControlComponent } from 'ng-zorro-antd/form';
import { NzRowDirective } from 'ng-zorro-antd/grid';
import { NzColDirective } from 'ng-zorro-antd/grid';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzInputDirective } from 'ng-zorro-antd/input';
import { NzInputGroupComponent } from 'ng-zorro-antd/input';
import { NzInputGroupWhitSuffixOrPrefixDirective } from 'ng-zorro-antd/input';
import { NzSegmentedComponent } from 'ng-zorro-antd/segmented';
import { MapService } from '../map/map.service';
import { PageComponent } from '../shared/components/page/page.component';
import { SearchComponent } from './advanced/search.component';
import { ExploreRoutesComponent } from './explore-routes.component';

@Component({
  selector: 'ui-explore',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
      <nz-input-group [nzSuffix]="suffixIconSearch">
        <input type="text" nz-input placeholder="input search text" />
      </nz-input-group>
      <ng-template #suffixIconSearch>
        <nz-icon nzType="search" />
      </ng-template>

      <nz-segmented [nzOptions]="options" (nzValueChange)="handleValueChange($event)" />
      <label nz-checkbox>Advanced</label>
      <div>
        <button nz-button nzType="primary">
          <nz-icon nzType="search" />
          Search
        </button>
      </div>
      <form [formGroup]="form" class="kpn-form" #ngForm="ngForm">
        <nz-form-item>
          <nz-form-label nzRequired nzFor="query"> Search</nz-form-label>
          <nz-form-control>
            <input nz-input id="query" placeholder="location, relation id" [formControl]="query" />
          </nz-form-control>
        </nz-form-item>
        <div>
          <button nz-button (click)="search()">Search</button>
        </div>
      </form>
      <div class="kpn-spacer-above">
        <button nz-button (click)="listRoutesInMap()">list routes in map</button>
      </div>
      <ui-divider class="kpn-spacer-above" />
      <ui-search />
      <ui-divider />

      @if (geocoderLocations().length > 0) {
        <ui-divider />
        <p>Locations</p>
        <ui-list>
          @for (location of geocoderLocations(); track location.name) {
            <ui-list-item [clickable]="true">
              {{ location.name }}
            </ui-list-item>
          }
        </ui-list>
        <ui-divider />
      }
      <ui-explore-routes />
    </ui-page>
  `,
  imports: [
    BreadcrumbComponent,
    DividerComponent,
    ExploreRoutesComponent,
    FormsModule,
    ListComponent,
    ListItemComponent,
    NzButtonComponent,
    NzCheckboxComponent,
    NzColDirective,
    NzFormControlComponent,
    NzFormItemComponent,
    NzFormLabelComponent,
    NzIconDirective,
    NzInputDirective,
    NzInputGroupComponent,
    NzInputGroupWhitSuffixOrPrefixDirective,
    NzRowDirective,
    NzSegmentedComponent,
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

  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    { label: Breadcrumbs.exploreLabel },
  ];

  static readonly home: BreadcrumbItem = {
    routerLink: '/',
    label: Breadcrumbs.homeLabel,
  };

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
