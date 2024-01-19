import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignupComponent } from './components/auth/signup/signup.component';
import { LoginComponent } from './components/auth/login/login.component';
import { HomeComponent } from './components/main/home/home.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'sign-up', component: SignupComponent },
  { path: 'user-profile', component: SignupComponent },
  { path: 'login', component: LoginComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutes {
}
