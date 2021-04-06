import {IndividualColumnFilter} from '../models/individual-column-filter';

export class Constants {
 public static MULTIPLE_SPACE_REGEX = new RegExp('\\s+', 'g');
  public static BASE_URL = window['BASE_HREF'];
  public static STORAGE_TYPE = localStorage;
  public static DEFAULT_APPNAME = 'Project factory';
  public static requiredTrueObject: IndividualColumnFilter = new IndividualColumnFilter().setIsRequired(true);
  public static USER_ID = '_empId';
  public static USER_NAME = '_un';
  public static FIRST_NAME = '_fn';
  public static LAST_NAME = '_ln';
  public static FULL_NAME = '_fn';
  public static RESET_REQ = '_pwd_reset_req';

  public static getServerURL() {
    return 'http://localhost:8087/';
  }
}

export const UNAUTHERIZED_PATH = ['/login', '/forgot-password', '/reset-password'];
