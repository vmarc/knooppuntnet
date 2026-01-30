import { Router } from '@angular/router';
import { NetworkAttributes } from '@api/common/network/network-attributes';
import { NetworkMarker } from '@app/analysis/subset/internal/networks/map/network-marker';
import { Popup } from 'maplibre-gl';
import { Marker } from 'maplibre-gl';

export class NetworkMarkerBuilder {
  static build(router: Router, network: NetworkAttributes): NetworkMarker {
    const networkLinkEventListener: EventListener = (e) =>
      router.navigateByUrl('/analysis/network/' + network.id);
    const networkLinkElement = this.buildNetworkLinkElement(network, networkLinkEventListener);
    const marker = this.buildMarker(network, networkLinkElement);
    return new NetworkMarker(marker, networkLinkElement, networkLinkEventListener);
  }

  private static buildNetworkLinkElement(
    network: NetworkAttributes,
    networkLinkEventListener: EventListener
  ): HTMLDivElement {
    const networkLinkElement = document.createElement('div');
    const link = $localize`:@@subset-map.dialog.show-network-details:Show network details`;
    networkLinkElement.innerHTML = `<p><a id="${network.id}">${link}</a></p>`;
    networkLinkElement.addEventListener('click', networkLinkEventListener);
    return networkLinkElement;
  }

  private static buildMarker(
    network: NetworkAttributes,
    networkLinkElement: HTMLDivElement
  ): Marker {
    const center = network.center;
    const marker = new Marker();
    marker.setLngLat([+center.longitude, +center.latitude]);
    marker.addClassName('kpn-marker');
    const popup = this.buildPopup(network, networkLinkElement);
    marker.setPopup(popup);
    return marker;
  }

  private static buildPopup(network: NetworkAttributes, networkLinkElement: HTMLDivElement): Popup {
    const popupContent = document.createElement('div');
    popupContent.innerHTML = this.buildPopupTemplate(network);
    popupContent.appendChild(networkLinkElement);
    return new Popup({
      focusAfterOpen: false,
    }).setDOMContent(popupContent);
  }

  private static buildPopupTemplate(network: NetworkAttributes): string {
    const nodes = $localize`:@@subset-map.dialog.nodeCount:nodes`;
    const routes = $localize`:@@subset-map.dialog.routeCount:routes`;
    return `
      <div class="kpn-marker-popup">
        <p><b>${network.name}</b></p>
        <p>
          ${network.km} km<br/>
          ${network.nodeCount} ${nodes}<br/>
          ${network.routeCount} ${routes}
        </p>
      </div>`;
  }
}
