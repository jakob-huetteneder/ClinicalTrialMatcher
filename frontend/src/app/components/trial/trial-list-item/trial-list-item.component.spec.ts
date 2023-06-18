import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrialListItemComponent } from './trial-list-item.component';

describe('TrialListItemComponent', () => {
  let component: TrialListItemComponent;
  let fixture: ComponentFixture<TrialListItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TrialListItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TrialListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
