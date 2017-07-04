package com.appointmentsched.scheduler;

import com.appointmentsched.scheduler.Scheduler.Appointment;

/**
 * Class DefaultPredicate simply accepts all appointments.
 *
 * @author Jeffrey Bour
 */
class DefaultPredicate extends Predicate {

  @Override
  public boolean accept(Appointment appointment) {
    return true;
  }

}