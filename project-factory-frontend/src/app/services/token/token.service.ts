import {Injectable} from '@angular/core';
import {StorageService} from '../storage/storage.service';
import {Constants} from '../../constant/constants';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  constructor(private storageService: StorageService) {}

  getToken() {
    return this.storageService.get('authToken');
  }

  setToken(token: string) {
    this.storageService.set('authToken', token);
  }

  removeToken(canClearAll?: boolean) {
    this.storageService.remove('authToken');
    this.storageService.remove(Constants.FIRST_NAME);
    this.storageService.remove(Constants.LAST_NAME);
    this.storageService.remove(Constants.USER_NAME);
  }
}
