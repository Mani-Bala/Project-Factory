import {Injectable} from '@angular/core';
import {CanActivate} from '@angular/router';
import {TokenService} from "../token/token.service";
import {AppUtilsService} from "../utils/app-utils.service";
import {Constants} from "../../constant/constants";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private utlis: AppUtilsService, private tokenService: TokenService) {}

  canActivate() {
    if (!this.tokenService.getToken()) {
      const url: URL = new URL(window.location.origin);
      this.utlis.openExternalURL(url.toString());
      return false;
    } else {
      return true;
    }
  }
}
