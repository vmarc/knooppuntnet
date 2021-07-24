import { Injectable } from '@angular/core';

@Injectable()
export class VersionService {
  version = '3.2.0-snapshot.3';
  experimental = true; // see also: network-vector-tile-layer.ts tile url !!
}
