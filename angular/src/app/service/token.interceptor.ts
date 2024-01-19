import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import {
  BehaviorSubject,
  catchError,
  filter,
  Observable,
  switchMap,
  take,
  throwError
} from 'rxjs';
import { inject, Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { LoginResponse } from '../components/auth/login/login-response.payload';

@Injectable({
  providedIn: 'root'
})
export class TokenInterceptor implements HttpInterceptor {
  isTokenRefreshing = false;

  private refreshTokenSubject: BehaviorSubject<string> = new BehaviorSubject<string>('');

  authService = inject(AuthService);

  intercept(
    req: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    if (req.url.indexOf('refresh') !== -1 || req.url.indexOf('login') !== -1) {
      return next.handle(req);
    }
    const jwtToken = this.authService.getJwtToken();
    console.log(jwtToken);
    if (jwtToken) {
      return next.handle(req.clone({ headers: req.headers.set('Authorization', `Bearer ${jwtToken}`) })).pipe(
        catchError((error) => {
          if (error instanceof HttpErrorResponse && error.status === 403) {
            return this.handleAuthErrors(req, next);
          }
          if (error instanceof HttpErrorResponse && error.status === 401) {
            return this.handle401Error(req, next);
          }
          return throwError(() => error);
        })
      );
    }
    return next.handle(req);
  }

  private handle401Error(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (!this.isTokenRefreshing) {
      this.isTokenRefreshing = true;
      this.refreshTokenSubject.next('');
      return this.authService.refreshToken().pipe(
        switchMap((refreshTokenResponse: LoginResponse) => {
          this.isTokenRefreshing = false;
          this.refreshTokenSubject.next(refreshTokenResponse.authenticationToken);
          return next.handle(
            request.clone({
              setHeaders: {
                Authorization: `Bearer ${refreshTokenResponse.authenticationToken}`
              }
            })
          );
        })
      );
    }
    return this.refreshTokenSubject.pipe(
      filter((result) => result !== null),
      take(1),
      switchMap((token) => next.handle(request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      })))
    );
  }

  private handleAuthErrors(
    req: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    if (!this.isTokenRefreshing) {
      this.isTokenRefreshing = true;
      this.refreshTokenSubject.next('');

      return this.authService.refreshToken().pipe(
        switchMap((refreshTokenResponse: LoginResponse) => {
          this.isTokenRefreshing = false;
          this.refreshTokenSubject.next(
            refreshTokenResponse.authenticationToken
          );
          return next.handle(req.clone({
            headers: req.headers.set('Authorization', `Bearer ${refreshTokenResponse.authenticationToken}`)
          }));
        })
      );
    }
    return this.refreshTokenSubject
    .pipe(filter((result) => result !== null), take(1), switchMap(() => next
    .handle(req.clone({ headers: req.headers.set('Authorization', `Bearer ${this.authService.getJwtToken()}`) }))));
  }
}
