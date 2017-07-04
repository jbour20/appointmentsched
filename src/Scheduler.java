package com.appointmentsched.scheduler;

import java.time.LocalDateTime;

import java.util.List;

/**
 * A minimal scheduler api. Interface contains implementations of Appointment
 * and AppointmentType classes it manages.
 *
 * @author Jeffrey Bour
 */
public interface Scheduler {

  /**
   * @return list of all scheduled appointments.
   */
  List<Appointment> listAllAppointments();

  /**
   * @return list of currently scheduled appointments ending after
   * LocalDateTime.now().
   */
  List<Appointment> listUpcomingAppointments();

  /**
   * @param appointment to check against.
   * @return list of currently scheduled appointments conflicting
   * with specified appointment.
   */
  List<Appointment> listConflictingAppointments(Appointment appointment);

  /**
   * Attempts to schedule specified appointment.
   * @param appoitnment to schedule.
   * @return true if successful, false if one or more scheduling conflicts
   * are detected.
   */
  boolean scheduleAppointment(Appointment appointment);

  /**
   * Attempts to remove specified appointment from scheduler.
   * @param appointment to remove from scheduler.
   * @return true if successful, false if appointment did not exist
   * in scheduler.
   */
  boolean cancelAppointment(Appointment appointment);

  /**
   * Simple enum consisting of valid appointment types and corresponding
   * durations in minutes.
   */
  enum AppointmentType {

    HAIRCUT(30l),
    SHAMPOO(60l);

    private final long duration;

    AppointmentType(long duration) {
      this.duration = duration;
    }

    public long duration() {
      return duration;
    }

  }

  /**
   * Appointment class is a simple container for appointment type,
   * start and end times. All fields are immutable.
   */
  static class Appointment {

    private final AppointmentType type;

    private final LocalDateTime start;
    private final LocalDateTime end;

    private static final String NULL_TYPE =
        "Parameter \'type\' cannot be null";
    private static final String NULL_DATETIME =
        "Parameter \'start\' cannot be null";

    public Appointment(AppointmentType type, LocalDateTime start) {
      if (type == null) {
        throw new IllegalArgumentException(NULL_TYPE);
      }
      if (start == null) {
        throw new IllegalArgumentException(NULL_DATETIME);
      }
      this.type = type;
      this.start = start;
      end = start.plusMinutes(type.duration);
    }

    public AppointmentType type() {
      return type;
    }

    public LocalDateTime startTime() {
      return start;
    }

    public LocalDateTime endTime() {
      return end;
    }

    /**
     * Checks to see if this appointment conflicts with specified appointment.
     * @param appointment to check against.
     * @return true if appointments overlap, false otherwise.
     */
    public boolean doesConflict(Appointment appointment) {
      if (start.compareTo(appointment.startTime()) > 0) {
        return start.compareTo(appointment.endTime()) < 0;
      }
      return end.compareTo(appointment.startTime()) > 0;
    }

    @Override
    public boolean equals(Object object) {
      if (object == null) {
        return false;
      }
      if (!(object instanceof Appointment)) {
        return false;
      }
      final Appointment appointment = (Appointment) object;
      return start.equals(appointment.startTime()) &&
          type.equals(appointment.type());
    }

    @Override
    public String toString() {
      return type.toString() + " " + DateUtils.format(start) +
          " - " + DateUtils.format(end);
    }

  }

}