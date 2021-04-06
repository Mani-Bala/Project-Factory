import { Component, OnInit } from '@angular/core';
import {FORGOT_PASSWORD, LOGIN_END_POINT} from "../../constant/rest-endpoints";
import {HttpStatus} from "../../constant/enums.enum";
import {isNullOrUndefined} from "util";
import {Constants} from "../../constant/constants";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpService} from "../../services/http/http.service";
import {TokenService} from "../../services/token/token.service";
import {Title} from "@angular/platform-browser";
import {StorageService} from "../../services/storage/storage.service";
import {Employee} from "../../models/employee";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {
  email: string;
  private employeeData: Employee;
  private encryptedToken: string;

  constructor(
    private router: Router,
    private http: HttpService,
    private actRoute: ActivatedRoute,
    private tokenService: TokenService,
    private titleService: Title,
    private storageService: StorageService,
    private toaster: ToastrService,
  ) { }

  ngOnInit() {
    this.email = null;
    this.checkIsAlreadyLoggedIn()
  }

  backToLogin() {
    this.router.navigate(['login']);
  }

  passwordRequest() {
    if (this.email) {
      const userInfo = {
        emailId: this.email
      }
      this.http.post(FORGOT_PASSWORD, userInfo).subscribe(response => {
        if (response.status === HttpStatus.OK) {
          this.toaster.success('Temporary password are emailed to the registered Email Address.');
        } else {
          this.toaster.error('User does not exists');
        }
      }, error => {
        this.toaster.error('User does not exists');
      });
    }
  }

  checkIsAlreadyLoggedIn() {
    if(this.tokenService.getToken()) {
      this.router.navigate(['project/dashboard']);
    }
  }
}
