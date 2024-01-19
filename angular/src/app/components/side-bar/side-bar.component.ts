import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})
export class SideBarComponent {
  router = inject(Router);

  goToCreatePost() {
    return Promise.resolve(this.router.navigateByUrl('/create-post'));
  }

  goToCreateSubreddit() {
    return Promise.resolve(this.router.navigateByUrl('/create-subreddit'));
  }
}
