import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';

@Injectable()
export class AppService {

  constructor(private http: HttpClient) {
  }

  public getPage1() {
    return this.http.get("/api/page1").pipe(
      map(response => response)
    );
  }

  public getPage2() {
    return this.http.get("/api/page2").pipe(
      map(response => response)
    );
  }

  public getPage3() {
    return this.http.get("/api/page3").pipe(
      map(response => response)
    );
  }

}
