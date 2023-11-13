import { Injectable } from '@angular/core';
import { MatIconRegistry } from '@angular/material/icon';
import * as canvg from 'canvg';
import { Map } from 'immutable';
import { Observable, of } from 'rxjs';
import { map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class BitmapIconService {
  icons: Map<string, string> = Map<string, string>();

  constructor(private iconRegistry: MatIconRegistry) {}

  public getIcon(iconName: string): Observable<string> {
    const icon = this.icons.get(iconName);
    if (icon) {
      return of(icon);
    }
    return this.createIcon(iconName);
  }

  private createIcon(iconName: string): Observable<string> {
    return this.iconRegistry.getNamedSvgIcon(iconName).pipe(
      map((svgElement) => {
        const str: string = svgElement.outerHTML;
        const canvas: HTMLCanvasElement = document.createElement('canvas');
        // @ts-ignore
        canvg(canvas, str);
        return canvas.toDataURL('image/png');
      }),
      tap((icon) => {
        this.icons = this.icons.set(iconName, icon);
      })
    );
  }
}
