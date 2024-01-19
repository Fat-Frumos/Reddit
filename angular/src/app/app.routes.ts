import {RouterModule, Routes} from '@angular/router';
import {SignupComponent} from './components/auth/signup/signup.component';
import {NgModule} from '@angular/core';
import {LoginComponent} from './components/auth/login/login.component';

export const routes: Routes = [
  { path: 'sign-up', component: SignupComponent },
  { path: 'login', component: LoginComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutes {
}
