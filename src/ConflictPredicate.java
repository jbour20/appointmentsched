package com.appointmentsched.scheduler;

import com.appointmentsched.scheduler.Scheduler.Appointment;

/**
 * Class ConflictPredicate returns true if this.appointment conflicts
 * with specified appointment.
 *
 * @author Jeffrey Bour
 */
class ConflictPredicate extends Predicate {

  private final Appointment appointment;

  ConflictPredicate(Appointment appointment) {
    this.appointment = appointment;
  }

  @Override
  public boolean accept(Appointment appointment) {
    return this.appointment.doesConflict(appointment);
  }

}