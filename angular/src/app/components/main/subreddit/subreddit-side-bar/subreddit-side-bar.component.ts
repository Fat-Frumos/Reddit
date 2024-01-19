import { Component } from '@angular/core';
import { SubredditModel } from '../subreddit-response';
import { SubredditService } from '../../../../service/subreddit.service';

@Component({
  selector: 'app-subreddit-side-bar',
  templateUrl: './subreddit-side-bar.component.html',
  styleUrls: ['./subreddit-side-bar.component.css']
})
export class SubredditSideBarComponent {
  subreddits: Array<SubredditModel> = [];

  displayViewAll: boolean = false;

  constructor(private subredditService: SubredditService) {
    this.subredditService.getAllSubreddits().subscribe((data) => {
      if (data.length > 3) {
        this.subreddits = data.splice(0, 3);
        this.displayViewAll = true;
      } else {
        this.subreddits = data;
      }
    });
  }
}
