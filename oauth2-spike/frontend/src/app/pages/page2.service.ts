import { HttpClient } from "@angular/common/http";
import { signal } from "@angular/core";
import { inject } from "@angular/core";
import { Injectable } from "@angular/core";
import { tap } from "rxjs";

@Injectable()
export class Page2Service {
  private readonly http = inject(HttpClient);
  private readonly _page = signal<string | null>(null);
  readonly page = this._page.asReadonly();

  constructor() {
    this.http
    .get("/api/page2", { responseType: "text" })
    .pipe(
      tap((response) => {
        this._page.set(response);
      }),
    )
    .subscribe();
  }
}
