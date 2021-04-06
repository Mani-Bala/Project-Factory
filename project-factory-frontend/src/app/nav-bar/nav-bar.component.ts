import {Component, OnInit} from '@angular/core';
import {Constants, UNAUTHERIZED_PATH} from "../constant/constants";
import {HttpService} from "../services/http/http.service";
import {ActivatedRoute} from "@angular/router";
import {TokenService} from "../services/token/token.service";
import {StorageService} from "../services/storage/storage.service";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {
  BASE_HREF = Constants.BASE_URL;
  userName: string
  constructor(
    private http: HttpService,
    private actRoute: ActivatedRoute,
    private tokenService: TokenService,
    private storageService: StorageService,
  ) { }

  ngOnInit() {
    this.userName = this.storageService.get(Constants.FULL_NAME);
  }

  logout() {
    this.tokenService.removeToken();
    window.location.href = this.BASE_HREF;
  }

  checkIfCurrentPathIsInUnAuthLocation() {
    if (!this.userName) {
      this.userName = this.storageService.get(Constants.FULL_NAME);
    }
    let path = window.location.pathname;
    return UNAUTHERIZED_PATH.includes(path);
  }
}
