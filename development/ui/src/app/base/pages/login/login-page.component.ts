import {Component} from "@angular/core";
import {PageService} from "../../../components/shared/page.service";
import {UserService} from "../../../user.service";

@Component({
  selector: "kpn-login-page",
  template: `
    <h1>
      Login
    </h1>

    <div class="note">
      <p>
        Login allows you to view extra information, such as changeset details, network history,
        route history and node history.
      </p>
      <p>
        This information is available to registered OpenStreetMap users only.
      </p>
      <p>
        After clicking the "login" button below, you will be directed (via the OpenStreetMap login page
        if you are not logged in to OpenStreetMap yet) to the OpenStreetMap
        page "Authorize access to your account". Click the button "Grant access"
        (leave the option "read your user preferences" checked).
      </p>
      <p>
        The knooppuntnet application only reads your username from the user preferences to establish that
        you are a registered OpenStreetMap user. All other user preferences are ignored,
        and not stored in knooppuntnet.
      </p>
      <p>
        This login procedure uses OpenStreetMap security.
        Knooppuntnet does not have access to your password at any moment.
      </p>
      <p>
        You remain logged in (also after closing you browser), until you explicitly logout through the logout page.
        To login again after logout you will have to use the same login procedure again.
      </p>
    </div>

    <button mat-raised-button color="primary" (click)="login()">Login</button>
  `,
  styles: [`
    .note {
      padding-top: 20px;
      padding-bottom: 40px;
      font-style: italic;
    }
  `]
})
export class LoginPageComponent {

  constructor(private userService: UserService,
              private pageService: PageService) {
  }

  login() {
    this.pageService.defaultMenu();
    this.userService.login();
  }

}
