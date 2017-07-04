package com.appointmentsched.scheduler;

import com.appointmentsched.scheduler.Scheduler.Appointment;

import java.time.LocalDateTime;

/**
 * Class ElapsedPredicate returns true if dateTime occurs after specified
 * appointment's endtime.
 *
 * @author Jeffrey Bour
 */
class ElapsedPredicate extends Predicate {

  private final LocalDateTime dateTime;

  ElapsedPredicate(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }

  @Override
  public boolean accept(Appointment appointment) {
    return dateTime.compareTo(appointment.endTime()) > 0;
  }

}