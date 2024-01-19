import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {LoginRequestPayload} from './login-request.payload';
import {AuthService} from '../shared/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  isError: boolean;
  loginForm: FormGroup;
  registerSuccessMessage: string;
  loginRequestPayload: LoginRequestPayload;

  constructor(
    private authService: AuthService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toast: ToastrService) {
    this.isError = false;
    this.registerSuccessMessage = '';
    this.loginRequestPayload = {
      username: '',
      password: ''
    };
    this.loginForm = new FormGroup({
      username: new FormControl(''),
      password: new FormControl('')
    });
  }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    });

    this.activatedRoute.queryParams
    .subscribe(params => {
      if (params['registered'] !== undefined && params['registered'] === 'true') {
        this.toast.success('Signup Successful');
        this.registerSuccessMessage = 'Please Check your inbox for activation email '
          + 'activate your account before you Login!';
      }
    });
  }

  login() {
    this.loginRequestPayload.username = this.loginForm.get('username')!.value;
    this.loginRequestPayload.password = this.loginForm.get('password')!.value;
    this.authService.login(this.loginRequestPayload).subscribe({
      next: data => {
        console.log(data);
        this.isError = false;
        void this.router.navigateByUrl('');
        this.toast.success('Login Successful');
      },
      error: error => {
        this.isError = true;
        console.log(error);
        this.toast.error('Login Failed! Please try again');
      }
    });
  }
}
