import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {Observable} from "rxjs";
import {XliffParser} from "./domain/xliff-parser";
import {TranslationUnit} from "./domain/translation-unit";

@Injectable()
export class TranslationsService {

  constructor(private http: HttpClient) {
  }

  public translationUnits(): Observable<TranslationUnit[]> {
    const url = "https://raw.githubusercontent.com/vmarc/knooppuntnet/develop/ui/src/locale/messages.fr.xlf";
    return this.http.get(url, {responseType: 'text'}).pipe(
      map(response => {
        return new XliffParser().parse(response);
      })
    );
  }

}
