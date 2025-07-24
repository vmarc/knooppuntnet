import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Country } from '@api/common/country';
import { RouteType } from '@api/common/route-type';
import { LocationPageHeaderComponent } from '@app/analysis/location/internal/components/location-page-header.component';
import { LocationService } from '@app/analysis/location/internal/location.service';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';

@Component({
  selector: 'ui-location',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-location-page-header [pageName]="pageName()" />
      <ui-error />
      <router-outlet />
    </ui-page>
  `,
  imports: [RouterOutlet, PageComponent, ErrorComponent, LocationPageHeaderComponent],
})
export class LocationComponent implements OnInit {
  private locationService = inject(LocationService);

  readonly routeType = input<RouteType>();
  readonly country = input<Country>();
  readonly location = input<string>();

  protected pageName = this.locationService.pageName;

  ngOnInit(): void {
    this.locationService.onInit(this.routeType(), this.country(), this.location());
  }
}
