import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {TranslationFile} from "./domain/translation-file";
import {XliffParser} from "./domain/xliff-parser";
import {TranslationUnit} from "./domain/translation-unit";

@Injectable()
export class TranslationsService {

  readonly root = "https://raw.githubusercontent.com/vmarc/knooppuntnet/master/client/src";

  constructor(private http: HttpClient) {
  }

  public loadTranslationFile(language: string): Observable<TranslationFile> {
    const url = `${this.root}/locale/translations.${language}.xlf`;
    return this.http.get(url, {responseType: "text"}).pipe(
      map(response => {
        return new XliffParser().parse(response);
      })
    );
  }

  public loadSource(translationUnit: TranslationUnit): Observable<string> {
    const url = `${this.root}/${translationUnit.sourceFile}`;
    return this.http.get(url, {responseType: "text"}).pipe(
      map(response => {
        const lineIndex = translationUnit.lineNumber;
        const lines = response.split("\n");
        const templateStartIndex = lines.findIndex(line => line.indexOf("template:") >= 0);
        const templateStart = templateStartIndex == -1 ? 0 : templateStartIndex - 1;
        const startLineNumber = templateStart + Math.max(0, lineIndex - 2);
        const endLineNumber = templateStart + lineIndex + 3;
        return lines.slice(startLineNumber, endLineNumber).join("\n");
      })
    );
  }

}
