import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {HttpService} from "../../services/http/http.service";
import {TokenService} from "../../services/token/token.service";
import {Title} from "@angular/platform-browser";
import {StorageService} from "../../services/storage/storage.service";
import {Constants} from "../../constant/constants";
import {RESET_PASSWORD} from "../../constant/rest-endpoints";
import {ToastrService} from "ngx-toastr";
import {HttpStatus} from "../../constant/enums.enum";

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  private newPassword: string;
  private confirmPassword: string;
  private showNewPwd: boolean;
  private showConfirmPwd: boolean;

  constructor(
    private router: Router,
    private http: HttpService,
    private actRoute: ActivatedRoute,
    private tokenService: TokenService,
    private titleService: Title,
    private storageService: StorageService,
    private toaster: ToastrService
  ) { }

  ngOnInit() {
    this.checkIsAlreadyLoggedIn();
  }

  backToLogin() {
    this.router.navigate(['login']);
  }

  resetPassword() {
    if (this.newPassword === this.confirmPassword) {
      const empId = Number(this.storageService.get(Constants.USER_ID));
      const changePwdObject = {
        id: empId,
        password: this.newPassword
      }
      this.http.put(RESET_PASSWORD, changePwdObject).subscribe(response => {
        if (response.status === HttpStatus.OK) {
          this.toaster.success('Password reset successfully')
          this.router.navigate(['project/dashboard']);
        } else {
          this.toaster.error('Password reset failed')
        }
      }, error => {
        this.toaster.error('Password reset failed')
      });
    } else {
      this.toaster.error('New password and confirm password both should be equal.');
    }
  }

  checkIsAlreadyLoggedIn() {
    let resetPwdRequested = this.storageService.get(Constants.RESET_REQ);
    if(this.tokenService.getToken() && !resetPwdRequested) {
      this.router.navigate(['project/dashboard']);
    }
  }
}
