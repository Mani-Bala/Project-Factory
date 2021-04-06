import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpRequest, HttpResponse} from '@angular/common/http';
// import { LoggerService } from '../logger/logger.service';
import {TokenService} from '../token/token.service';
import {HttpStatus} from '../../constant/enums.enum';
import {Observable} from 'rxjs';
import {isNullOrUndefined} from 'util';
import {tap} from 'rxjs/internal/operators';
import {AppUtilsService} from '../utils/app-utils.service';
import {StorageService} from '../storage/storage.service';

@Injectable({
  providedIn: 'root'
})
export class RequestInterceptorService {
  constructor(
    // private logger: LoggerService,
    private tokenService: TokenService,
    private utils: AppUtilsService,
    private storage: StorageService
  ) {}

  private errorCode: number;

  handleError(error: any) {
    if (error instanceof HttpErrorResponse) {
      if (!navigator.onLine) {
        // this.logger.error('Internet connectivity error occurred');
      } else {
        switch (error.status) {
          case HttpStatus.BAD_REQUEST:
            this.errorCode = HttpStatus.BAD_REQUEST;
            break;

          case HttpStatus.UNAUTHORIZED:
            this.errorCode = HttpStatus.UNAUTHORIZED;
            break;

          case HttpStatus.NOT_FOUND:
            this.errorCode = HttpStatus.NOT_FOUND;
            break;

          case HttpStatus.INTERNAL_SERVER_ERROR:
            this.errorCode = HttpStatus.INTERNAL_SERVER_ERROR;
            break;

          case HttpStatus.BAD_GATEWAY:
            this.errorCode = HttpStatus.BAD_GATEWAY;
            break;

          case HttpStatus.FORBIDDEN:
            this.errorCode = HttpStatus.FORBIDDEN;
            break;

          case HttpStatus.UNPROCESSABLE_ENTITY:
            this.errorCode = HttpStatus.UNPROCESSABLE_ENTITY;
            break;

          case HttpStatus.SERVICE_UNAVAILABLE:
            this.errorCode = HttpStatus.SERVICE_UNAVAILABLE;
            break;

          case HttpStatus.GATEWAY_TIMEOUT:
            this.errorCode = HttpStatus.GATEWAY_TIMEOUT;
            break;

          default:
            this.errorCode = 0;
            break;
        }

        // ----- Implementation can be added for handling errors ------
        // this.logger.error(this.errorCode);
      }
    }
  }

  refreshToken(headers) {
    if (headers.get('refreshedEncryptedToken') != null) {
      this.tokenService.setToken(headers.get('refreshedEncryptedToken'));
    }
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.tokenService.getToken();

    if (!isNullOrUndefined(token)) {
      if (request.headers.has('ContentType')) {
        const contentType = request.headers.get('ContentType');
        if (contentType === 'multipart/form-data') {
          request = request.clone({
            setHeaders: {
              encryptedToken: token
            }
          });
        }
      } else {
        request = request.clone({
          setHeaders: {
            encryptedToken: token,
            'Content-Type': 'application/json'
          }
        });
      }
    } else {
      request = request.clone({
        setHeaders: {
          'Content-Type': 'application/json'
        }
      });
    }

    return next.handle(request).pipe(
      tap(
        event => {
          if (event instanceof HttpResponse) {
            this.refreshToken(event.headers);
          }
        },
        error => {
          this.handleError(error);
        }
      )
    );
  }
}
