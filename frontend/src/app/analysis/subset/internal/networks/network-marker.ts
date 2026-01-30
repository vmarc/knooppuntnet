import { Router } from '@angular/router';
import { NetworkAttributes } from '@api/common/network/network-attributes';
import { Popup } from 'maplibre-gl';
import { Marker } from 'maplibre-gl';

export class NetworkMarker {
  readonly marker: Marker;
  private networkLinkElement: HTMLDivElement;
  private networkLinkEventListener: (PointerEvent) => void;

  constructor(router: Router, network: NetworkAttributes) {
    this.networkLinkEventListener = (e) => {
      router.navigateByUrl('/analysis/network/' + network.id);
    };
    this.marker = this.buildMarker(network);
  }

  remove(): void {
    this.networkLinkElement.removeEventListener('click', this.networkLinkEventListener);
    this.marker.remove();
  }

  private buildMarker(network: NetworkAttributes): Marker {
    const center = network.center;
    const marker = new Marker();
    marker.setLngLat([+center.longitude, +center.latitude]);
    marker.addClassName('kpn-marker');
    const popup = this.buildPopup(network);
    marker.setPopup(popup);
    return marker;
  }

  private buildPopup(network: NetworkAttributes): Popup {
    const popupContent = document.createElement('div');
    popupContent.innerHTML = this.buildPopupTemplate(network);
    this.networkLinkElement = document.createElement('div');
    this.networkLinkElement.innerHTML = `<p><a id="${network.id}">details</a></p>`;
    popupContent.appendChild(this.networkLinkElement);
    this.networkLinkElement.addEventListener('click', this.networkLinkEventListener);
    return new Popup({
      focusAfterOpen: false,
    }).setDOMContent(popupContent);
  }

  private buildPopupTemplate(network: NetworkAttributes): string {
    return `
      <div class="kpn-marker-popup">
        <p><b>${network.name}</b></p>
        <p>${network.km} km<br/>${network.nodeCount} nodes<br/>${network.routeCount} routes</p>
      </div>`;
  }
}
