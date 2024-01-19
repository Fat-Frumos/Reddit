import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  SubredditModel
} from '../components/main/subreddit/subreddit-response';

@Injectable({
  providedIn: 'root'
})
export class SubredditService {
  http = inject(HttpClient);

  baseUrl = 'https://key-cloak.onrender.com/api/';

  getAllSubreddits(): Observable<Array<SubredditModel>> {
    return this.http.get<Array<SubredditModel>>(`${this.baseUrl}subreddit`);
  }

  createSubreddit(subredditModel: SubredditModel): Observable<SubredditModel> {
    return this.http.post<SubredditModel>(`${this.baseUrl}subreddit`, subredditModel);
  }
}
