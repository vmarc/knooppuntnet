import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { UserService } from './user.service';
import { UserErrorComponent } from './user-error.component';

@Component({
  selector: 'kpn-user-logout',
  standalone: true,
  template: `
    <h1>Logout</h1>
    <p>Here goes some explanation about the logout process.</p>
    <div class="menu top-spacer">
      <button (click)="logout()">logout</button>
      <a (click)="cancel()">cancel</a>
    </div>
    <kpn-user-error />
  `,
  imports: [UserErrorComponent],
})
export class UserLogoutComponent {
  private readonly userService = inject(UserService);

  logout(): void {
    this.userService.logout();
  }

  cancel(): void {
    this.userService.navigateToReturnUrl();
  }
}
