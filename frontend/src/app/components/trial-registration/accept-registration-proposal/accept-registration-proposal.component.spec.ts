import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AcceptRegistrationProposalComponent } from './accept-registration-proposal.component';

describe('ViewRegistrationRequestsComponent', () => {
  let component: AcceptRegistrationProposalComponent;
  let fixture: ComponentFixture<AcceptRegistrationProposalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AcceptRegistrationProposalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AcceptRegistrationProposalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
