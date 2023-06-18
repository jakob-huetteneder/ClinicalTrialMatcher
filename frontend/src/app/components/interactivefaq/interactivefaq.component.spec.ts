import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InteractivefaqComponent } from './interactivefaq.component';

describe('InteractivefaqComponent', () => {
  let component: InteractivefaqComponent;
  let fixture: ComponentFixture<InteractivefaqComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InteractivefaqComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InteractivefaqComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
