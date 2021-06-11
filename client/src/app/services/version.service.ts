import { Injectable } from '@angular/core';

@Injectable()
export class VersionService {
  version = '3.1.12';
  experimental = false; // see also: network-vector-tile-layer.ts tile url !!
}
