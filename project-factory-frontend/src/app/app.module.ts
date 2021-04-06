import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {FooterComponent} from './footer/footer.component';
import {CreateProjectComponent} from './project/create-project/create-project.component';
import {DashboardComponent} from './project/dashboard/dashboard.component';
import {NavBarComponent} from './nav-bar/nav-bar.component';
import {MDBBootstrapModule} from 'angular-bootstrap-md';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SearchableDropdownComponent} from './searchable-dropdown/searchable-dropdown.component';
import {RouterModule, Routes} from "@angular/router";
import {CustomPreLoading} from "./custom-pre-loading";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {RequestInterceptorService} from "./services/http/request-interceptor.service";
import {AuthGuard} from "./services/guard/auth.guard";
import {ToastrModule} from 'ngx-toastr';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgxSpinnerModule} from 'ngx-spinner';
import {ForgotPasswordComponent} from "./login/forgot-password/forgot-password.component";
import {ResetPasswordComponent} from "./login/reset-password/reset-password.component";
import {LoginPageComponent} from "./login/login-page/login-page.component";

const appRoutes: Routes = [
  {
    path: 'login',
    component: LoginPageComponent
  },
  {
    path: 'forgot-password',
    component: ForgotPasswordComponent
  },
  {
    path: 'reset-password',
    component: ResetPasswordComponent,
    canActivate: [AuthGuard]
  },
  { path: 'project/create',
    component: CreateProjectComponent,
    canActivate: [AuthGuard]
  },
  { path: 'project/dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard]
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' },
  ];

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    LoginPageComponent,
    ForgotPasswordComponent,
    ResetPasswordComponent,
    CreateProjectComponent,
    DashboardComponent,
    NavBarComponent,
    SearchableDropdownComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    MDBBootstrapModule.forRoot(),
    RouterModule.forRoot(appRoutes, { preloadingStrategy: CustomPreLoading }),
    FormsModule,
    ReactiveFormsModule,
    ToastrModule.forRoot(),
    BrowserAnimationsModule,
    NgxSpinnerModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: RequestInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
