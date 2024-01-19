import { Component, Input, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { throwError } from 'rxjs';
import { faArrowUp, faArrowDown } from '@fortawesome/free-solid-svg-icons';
import { VotePayload } from '../vote-payload';
import { AuthService } from '../../../../service/auth.service';
import { PostService } from '../../../../service/post.service';
import { VoteService } from '../../../../service/vote.service';
import { PostModel } from '../../post/post.model';
import { VoteType } from '../vote.type';
import { VoteResponse } from '../vote-response';

@Component({
  selector: 'app-vote-button',
  templateUrl: './vote-button.component.html',
  styleUrls: ['./vote-button.component.css']
})
export class VoteButtonComponent implements OnInit {
  @Input() post: PostModel = {} as PostModel;

  votePayload: VotePayload;

  faArrowUp = faArrowUp;

  faArrowDown = faArrowDown;

  upvoteColor: string = '';

  downvoteColor: string = '';

  isLoggedIn: boolean = false;

  constructor(
    private voteService: VoteService,
    private authService: AuthService,
    private postService: PostService,
    private toast: ToastrService
  ) {
    this.votePayload = {
      voteType: VoteType.ZERO,
      postId: 0
    };
    this.authService.loggedIn.subscribe((data: boolean) => {
      this.isLoggedIn = data;
    });
  }

  ngOnInit(): void {
    this.updateVoteDetails();
  }

  upvotePost() {
    this.votePayload.voteType = VoteType.UPVOTE;
    this.vote();
    this.downvoteColor = '';
  }

  downvotePost() {
    this.votePayload.voteType = VoteType.DOWNVOTE;
    this.vote();
    this.upvoteColor = '';
  }

  private vote() {
    this.votePayload.postId = this.post.id;
    this.voteService.vote(this.votePayload).subscribe({
      next: (response: VoteResponse) => {
        console.log(response);
        this.updateVoteDetails();
      },
      error: (error) => {
        this.toast.error(error.error.message);
        return throwError(() => error);
      }
    });
  }

  private updateVoteDetails() {
    this.postService.getPost(this.post.id).subscribe((post) => {
      this.post = post;
    });
  }
}
