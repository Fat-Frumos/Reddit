import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  isLoggedIn: boolean;

  username: string;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    this.isLoggedIn = false;
    this.username = '';
  }

  ngOnInit() {
    this.authService.loggedIn.subscribe((data: boolean) => {
      this.isLoggedIn = data;
    });
    this.authService.username.subscribe((data: string) => {
      this.username = data;
    });
    this.isLoggedIn = this.authService.isLoggedIn();
    this.username = this.authService.getUserName();
  }

  goToUserProfile() {
    this.router.navigateByUrl(`/user-profile/${this.username}`);
  }

  logout() {
    this.authService.logout();
    this.isLoggedIn = false;
    this.router.navigateByUrl('');
  }
}
