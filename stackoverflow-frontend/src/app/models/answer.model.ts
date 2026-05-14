export interface AnswerUser {
  id: number;
  username?: string;
  name?: string;
  email?: string;
}

export interface AnswerQuestion {
  id: number;
  title?: string;
}

export interface Answer {
  id: number;
  author?: AnswerUser;
  question?: AnswerQuestion;
  text: string;
  imagine?: string;
  creationData?: string;
}
