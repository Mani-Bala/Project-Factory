import { Injectable } from '@angular/core';
import { Constants } from '../../constant/constants';
import { Observable } from 'rxjs';
import { isNullOrUndefined } from 'util';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  appData = {};

  constructor() {}

  storageType = Constants.STORAGE_TYPE;

  set(key: string, value: any): void {
    this.storageType.setItem(key, value);
  }

  setAsEncrypted(key: string, value: any): void {
    this.storageType.setItem(key, window.btoa(encodeURIComponent(value)));
  }

  setAll(key: string, value: any): void {
    this.storageType.setItem(key, JSON.stringify(value));
  }

  setAllAsEncrypted(key: string, value: any, shouldNotEncode?: boolean): void {
    if (!isNullOrUndefined(shouldNotEncode)) {
      this.storageType.setItem(key, window.btoa(JSON.stringify(value)));
    } else {
      this.storageType.setItem(key, window.btoa(encodeURIComponent(JSON.stringify(value))));
    }
  }

  get(key: string): any {
    return this.storageType.getItem(key);
  }

  getAsDecrypted(key: string): any {
    return isNullOrUndefined(this.storageType.getItem(key)) ? null : decodeURIComponent(window.atob(this.storageType.getItem(key)));
  }

  getAll(key: string): any {
    return isNullOrUndefined(this.storageType.getItem(key)) ? null : JSON.parse(this.storageType.getItem(key));
  }

  getAllAsDecrypted(key: string, shouldNotEncode?: boolean): any {
    return isNullOrUndefined(this.storageType.getItem(key))
      ? null
      : !isNullOrUndefined(shouldNotEncode)
      ? JSON.parse(window.atob(this.storageType.getItem(key)))
      : JSON.parse(decodeURIComponent(window.atob(this.storageType.getItem(key))));
  }

  remove(key: string): void {
    this.storageType.removeItem(key);
  }

  removeAll(): void {
    this.storageType.clear();
  }

  contains(key: string): boolean {
    return !isNullOrUndefined(this.get(key));
  }

  // ------------- App Storage (In memory) ------

  getAppData(key: string) {
    if (!key) {
      return null;
    }
    return this.appData[key];
  }

  removeAppData(key: string) {
    if (key) {
      this.appData[key] = undefined;
    }
  }

  setAppData(key: string, value: any) {
    if (key) {
      this.appData[key] = value;
    }
  }

  getSelectedBatchFromStorage() {
    // let selectedBatch = new Batch();
    // selectedBatch.coTrainers = [];
    // selectedBatch.qcGroupTrainer = [];
    // let coTrainer: string = this.get(Constants.SELECTED_BATCH_CO_TRAINERS);
    // if (!isNullOrUndefined(coTrainer)) {
    //   let coTrainerList = coTrainer.split(',');
    //   coTrainerList.forEach(item => {
    //     selectedBatch.coTrainers.push(new Employee().setId(item));
    //   });
    // }
    // let qcGroupTrainer: string = this.get(Constants.SELECTED_BATCH_QC_GROUP_TRAINERS);
    // if (!isNullOrUndefined(qcGroupTrainer)) {
    //   let qcGroupTrainerList = qcGroupTrainer.split(',');
    //   qcGroupTrainerList.forEach(item => {
    //     selectedBatch.qcGroupTrainer.push(new Employee().setId(item));
    //   });
    // }
    // let trainer: string = this.get(Constants.SELECTED_BATCH_TRAINER);
    // if (!isNullOrUndefined(trainer)) {
    //   selectedBatch.trainer = new Employee().setId(trainer);
    // }
    // return selectedBatch;
  }
}
