import { EventEmitter, Injectable, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import {
  SignupRequestPayload
} from '../components/auth/signup/singup-request.payload';
import {
  LoginRequestPayload
} from '../components/auth/login/login-request.payload';
import { LoginResponse } from '../components/auth/login/login-response.payload';
import { StorageService } from './storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  baseUrl = 'https://key-cloak.onrender.com/api/';

  @Output() loggedIn: EventEmitter<boolean>;

  @Output() username: EventEmitter<string>;

  constructor(
    private httpClient: HttpClient,
    private storage: StorageService
  ) {
    this.loggedIn = new EventEmitter();
    this.username = new EventEmitter();
  }

  get refreshTokenPayload() {
    return {
      refreshToken: this.getRefreshToken(),
      username: this.getUserName()
    };
  }

  signup(signupRequestPayload: SignupRequestPayload): Observable<string> {
    return this.httpClient.post(
      `${this.baseUrl}auth/signup`,
      signupRequestPayload,
      { responseType: 'text' }
    );
  }

  login(loginRequestPayload: LoginRequestPayload): Observable<boolean> {
    return this.httpClient.post<LoginResponse>(`${this.baseUrl}auth/login`, loginRequestPayload).pipe(
      map((data) => {
        if (!this.storage) {
          console.error('LocalStorage is not initialized');
          return false;
        }
        this.storage.store('authenticationToken', data.authenticationToken);
        this.storage.store('username', data.username);
        this.storage.store('refreshToken', data.refreshToken);
        this.storage.store('expiresAt', data.expiresAt.toString());
        this.loggedIn.emit(true);
        this.username.emit(data.username);
        return true;
      })
    );
  }

  getJwtToken(): string {
    if (!this.storage) {
      console.error('LocalStorage is not initialized');
      return '';
    }
    return this.storage.retrieve('authenticationToken');
  }

  refreshToken() {
    return this.httpClient.post<LoginResponse>(`${this.baseUrl}auth/refresh/token`, this.refreshTokenPayload).pipe(
      tap((response) => {
        this.storage.clear('authenticationToken');
        this.storage.clear('expiresAt');
        this.storage.store(
          'authenticationToken',
          response.authenticationToken
        );
        this.storage.store('expiresAt', response.expiresAt.toString());
      })
    );
  }

  logout() {
    this.httpClient.post(`${this.baseUrl}auth/logout`, this.refreshTokenPayload, {
      responseType: 'text'
    }).pipe(
      map((data) => {
        console.log(data);
        return data;
      }),
      catchError((error) => {
        console.error(error);
        return throwError(() => error);
      })
    ).subscribe(() => {
      this.storage.clear('authenticationToken');
      this.storage.clear('username');
      this.storage.clear('refreshToken');
      this.storage.clear('expiresAt');
    });
  }

  getUserName() {
    return this.storage.retrieve('username');
  }

  getRefreshToken() {
    return this.storage.retrieve('refreshToken');
  }

  isLoggedIn(): boolean {
    return this.getJwtToken() !== '';
  }
}
