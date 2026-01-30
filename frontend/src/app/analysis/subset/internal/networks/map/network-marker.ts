import { Marker } from 'maplibre-gl';

export class NetworkMarker {
  constructor(
    readonly marker: Marker,
    private networkLinkElement: HTMLDivElement,
    private networkLinkEventListener: (PointerEvent) => void
  ) {}

  remove(): void {
    this.networkLinkElement.removeEventListener('click', this.networkLinkEventListener);
    this.marker.remove();
  }
}
