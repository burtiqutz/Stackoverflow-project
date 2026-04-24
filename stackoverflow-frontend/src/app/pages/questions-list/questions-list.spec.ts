import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionsList } from './questions-list';

describe('QuestionsList', () => {
  let component: QuestionsList;
  let fixture: ComponentFixture<QuestionsList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuestionsList],
    }).compileComponents();

    fixture = TestBed.createComponent(QuestionsList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
