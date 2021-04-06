import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  constructor(private http: HttpClient) {}

  get(url: string, options?: any): Observable<any> {
    return this.http.get<any>(url, this.setOptions(options));
  }

  post(url: string, body: any, options?: any): Observable<any> {
    return this.http.post<any>(url, body, this.setOptions(options));
  }

  put(url: string, body: any, options?: any): Observable<any> {
    return this.http.put<any>(url, body, this.setOptions(options));
  }

  delete(url: string, options?: any): Observable<any> {
    return this.http.delete<any>(url, this.setOptions(options));
  }

  patch(url: string, body: any, options?: any): Observable<any> {
    return this.http.patch<any>(url, body, this.setOptions(options));
  }

  setOptions(options) {
    if (options) {
      options['observe'] = 'response';
    } else {
      options = { observe: 'response' };
    }
    return options;
  }
}
