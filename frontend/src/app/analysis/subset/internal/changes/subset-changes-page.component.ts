import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { SubsetChangesComponent } from './components/subset-changes.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { ErrorComponent } from '@app/shared/components/error/error.component';
import { RouterService } from '@app/shared/services/router.service';
import { UserLinkLoginComponent } from '@app/shared/user/user-link-login.component';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetChangesPageService } from './subset-changes-page.service';

@Component({
  selector: 'ui-subset-changes-page',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <ui-page>
      <ui-subset-page-header-block
        pageName="changes"
        pageTitle="Changes"
        i18n-pageTitle="@@subset-changes.title"
      />

      <ui-error />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (service.loggedIn() === false) {
            <p i18n="@@subset-changes.login-required">
              This details of the changes history are available to logged in OpenStreetMap
              contributors only.
            </p>
            <p>
              <ui-user-link-login />
            </p>
          } @else {
            <ui-subset-changes />
          }
        </div>
      }
    </ui-page>
  `,
  providers: [SubsetChangesPageService, RouterService],
  imports: [
    ErrorComponent,
    SubsetPageHeaderBlockComponent,
    UserLinkLoginComponent,
    PageComponent,
    SubsetChangesComponent,
  ],
})
export class SubsetChangesPageComponent implements OnInit {
  protected readonly service = inject(SubsetChangesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
