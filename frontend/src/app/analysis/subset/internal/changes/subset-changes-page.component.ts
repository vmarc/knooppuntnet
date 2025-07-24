import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { SubsetChangesComponent } from './components/subset-changes.component';
import { UserLinkLoginComponent } from '@app/shared/user/user-link-login.component';
import { SubsetChangesPageService } from './subset-changes-page.service';

@Component({
  selector: 'ui-subset-changes-page',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
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
  `,
  providers: [SubsetChangesPageService],
  imports: [SubsetChangesComponent, UserLinkLoginComponent],
})
export class SubsetChangesPageComponent implements OnInit {
  protected readonly service = inject(SubsetChangesPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
