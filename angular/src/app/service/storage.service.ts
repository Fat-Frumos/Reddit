import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  private storage: Storage;

  constructor() {
    this.storage = window.localStorage;
  }

  retrieve(key: string): string {
    const item = this.storage.getItem(key);
    return item ? JSON.parse(item) : '';
  }

  store(key: string, value: string): void {
    this.storage.setItem(key, JSON.stringify(value));
  }

  clear(key: string) {
    this.storage.setItem(key, '');
  }
}
