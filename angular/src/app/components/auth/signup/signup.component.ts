import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators
} from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { AuthService } from '../../../service/auth.service';
import { SignupRequestPayload } from './singup-request.payload';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  signupRequestPayload: SignupRequestPayload;

  signupForm: FormGroup;

  constructor(
    private authService: AuthService,
    private router: Router,
    private formBuilder: FormBuilder,
    private toast: ToastrService
  ) {
    this.signupRequestPayload = {
      username: '',
      email: '',
      password: ''
    };
    this.signupForm = this.formBuilder.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.signupForm = new FormGroup({
      username: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required)
    });
  }

  signup() {
    const emailControl = this.signupForm?.get('email');
    const usernameControl = this.signupForm?.get('username');
    const passwordControl = this.signupForm?.get('password');

    if (emailControl && usernameControl && passwordControl) {
      this.signupRequestPayload.email = emailControl.value || '';
      this.signupRequestPayload.username = usernameControl.value || '';
      this.signupRequestPayload.password = passwordControl.value || '';

      this.authService.signup(this.signupRequestPayload).subscribe({
        next: (data) => {
          console.log(data);
          this.router.navigate(['/login'], {
            queryParams: { registered: 'true' }
          });
        },
        error: (error) => {
          console.log(error);
          this.toast.error('Registration Failed! Please try again');
        }
      });
    }
  }
}
