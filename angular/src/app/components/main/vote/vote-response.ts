import { VoteType } from './vote.type';

export interface VoteResponse {
  voteId: number;
  post: {
    postId: number;
  };
  user: {
    userId: number;
  };
  voteType: VoteType;
}
