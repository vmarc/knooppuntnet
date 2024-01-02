import { DOCUMENT } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { UserSession } from '@api/common/common/user-session';
import { UserStore } from './user.store';
import * as Sentry from '@sentry/angular-ivy';

@Injectable()
export class UserService {
  private readonly http = inject(HttpClient);
  private readonly userStore = inject(UserStore);
  private readonly document = inject(DOCUMENT);

  constructor() {
    this.http.get('/oauth2/user', { responseType: 'text' }).subscribe((user) => {
      if (user && user.length > 0) {
        this.updateUser(user);
      }
    });
  }

  login(): void {
    this.document.location.assign('/oauth2/authorization/osm');
  }

  logout(): void {
    this.http.post('/oauth2/logout', { responseType: 'text' }).subscribe({
      next: () => this.updateUser(null),
      error: () => this.updateUser(null),
    });
  }

  users() {
    this.http.get<UserSession[]>('/oauth2/users').subscribe((sessions) => {
      console.log('sessions');
      sessions.forEach((session) => console.log(`   ${JSON.stringify(session)}`));
    });
  }

  private updateUser(user: string | null): void {
    if (user) {
      Sentry.setUser({
        _id: user,
      });
    } else {
      Sentry.setUser(null);
    }
    this.userStore.updateUser(user);
  }
}
