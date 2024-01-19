import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { VotePayload } from '../components/main/vote/vote-payload';
import { VoteResponse } from '../components/main/vote/vote-response';

@Injectable({
  providedIn: 'root'
})
export class VoteService {
  baseUrl = 'https://key-cloak.onrender.com/api/';

  http = inject(HttpClient);

  vote(votePayload: VotePayload): Observable<VoteResponse> {
    return this.http.post<VoteResponse>(`${this.baseUrl}vote`, votePayload);
  }
}
