import { Injectable } from '@angular/core';

@Injectable()
export class VersionService {
  version = '3.2.2';
  experimental = true; // see also: network-vector-tile-layer.ts, network-bitmap-tile-layer.ts and network-nodes-tile-layer.ts tile urls !!
}
