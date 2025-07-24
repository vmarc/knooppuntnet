import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Country } from '@api/common/country';
import { Fact } from '@api/common/fact';
import { RouteType } from '@api/common/route-type';
import { SubsetPageHeaderBlockComponent } from '@app/analysis/subset/internal/components/subset-page-header-block.component';
import { SubsetService } from '@app/analysis/subset/internal/subset.service';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { PageComponent } from '@app/shared/components/page/page.component';

@Component({
  selector: 'ui-subset',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-subset-page-header-block [pageName]="pageName()" />
      <ui-error />
      <router-outlet />
    </ui-page>
  `,
  imports: [PageComponent, RouterOutlet, ErrorComponent, SubsetPageHeaderBlockComponent],
})
export class SubsetComponent implements OnInit {
  private subsetService = inject(SubsetService);
  readonly country = input<Country>();
  readonly routeType = input<RouteType>();
  readonly fact = input<Fact>(undefined);

  protected readonly pageName = this.subsetService.pageName;

  ngOnInit(): void {
    this.subsetService.onInit(this.country(), this.routeType());
  }
}
