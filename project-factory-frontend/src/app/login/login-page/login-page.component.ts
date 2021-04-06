import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpService} from "../../services/http/http.service";
import {ActivatedRoute, Router} from "@angular/router";
import {isNullOrUndefined} from "util";
import {LOGIN_END_POINT} from "../../constant/rest-endpoints";
import {Employee} from "../../models/employee";
import {TokenService} from "../../services/token/token.service";
import {Title} from "@angular/platform-browser";
import {StorageService} from "../../services/storage/storage.service";
import {Constants} from "../../constant/constants";
import {HttpStatus} from "../../constant/enums.enum";
import {MessageRenderer} from "../../models/message-renderer";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {
  email: string;
  password: string;
  isLoggedIn: boolean;
  isLogInFail: boolean;
  showPassword: boolean;
  employeeData: Employee;
  private token: string;
  message: MessageRenderer;
  canRemember: boolean;

  constructor(
    private router: Router,
    private http: HttpService,
    private actRoute: ActivatedRoute,
    private tokenService: TokenService,
    private titleService: Title,
    private storageService: StorageService) {}

  ngOnInit() {
    this.showPassword = false;
    this.message = null;
    this.checkIsAlreadyLoggedIn();
  }

  signIn() {
    this.message = new MessageRenderer();
    if(!isNullOrUndefined(this.email) && !isNullOrUndefined(this.password)) {
      const credentials = {
        emailId: this.email,
        password: this.password
      }
      this.http.post(LOGIN_END_POINT, credentials).subscribe(response => {
        if (response.status === HttpStatus.OK && !isNullOrUndefined(response.body.data)) {
          this.employeeData = response.body.data;
          if (this.employeeData && this.employeeData.encryptedLoginToken) {
            this.isLoggedIn = true;
            this.isLogInFail = false;
            this.tokenService.removeToken(true);
            this.token = this.employeeData.encryptedLoginToken.split(' ').join('+');
            this.tokenService.setToken(this.token);
            this.storageService.set(Constants.USER_NAME, window.btoa(this.employeeData.emailId));
            this.storageService.set(Constants.USER_ID, this.employeeData.id);
            this.storageService.set(Constants.FIRST_NAME, this.employeeData.firstName);
            this.storageService.set(Constants.LAST_NAME, this.employeeData.lastName);
            this.storageService.set(Constants.FULL_NAME, this.employeeData.displayFullName);
            this.storageService.set(Constants.RESET_REQ, this.employeeData.passwordResetRequest);
            this.message.type = 'SUCCESS';
            this.message.message = response.body.description;
            if (this.employeeData.passwordResetRequest) {
              this.router.navigate(['reset-password']);
            } else {
              this.router.navigate(['project/dashboard']);
            }
          }
        } else {
          this.message.type = 'ERROR';
          this.message.message = 'Login failed';
        }
      }, err => {
        this.message.type = 'ERROR';
        this.message.message = err.error.description;
      });
    }
    this.clearFields();
  }

  checkIsAlreadyLoggedIn() {
    if(this.tokenService.getToken()) {
      this.router.navigate(['project/dashboard']);
    }
  }

  clearFields() {
    this.email = '';
    this.password = '';
  }
}
