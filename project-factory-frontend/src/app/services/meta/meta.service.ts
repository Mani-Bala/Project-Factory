import { Injectable } from '@angular/core';
import { Meta } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class MetaService {
  constructor(private meta: Meta) {}

  addRobotsTag() {
    this.meta.addTag({ name: 'robots', content: 'noindex, nofollow' });
  }

  removeRobotsTag() {
    this.meta.removeTag('name="robots"');
  }
}
