package com.appointmentsched.scheduler;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * Class Schedule contains an implementation of the Scheduler interface.
 *
 * @author Jeffrey Bour
 */
public class Schedule implements Scheduler {

  private static final String NULL_APPOINTMENT =
      "Parameter \'appointment\' cannot be null";
  private static final String NULL_DATETIME =
      "Parameter \'dateTime\' cannot be null";

  // Use a Comparator here, since a Comparable implementation would need to
  // depend on type and start time to be consistent with equals.
  private static final Comparator<Appointment> AppointmentComparator = 
      new Comparator<Appointment>() {
        public int compare(Appointment appointment, Appointment other) {
          return appointment.startTime().compareTo(other.startTime());
        }
      };

  private final NavigableSet<Appointment> schedule =
      new TreeSet<Appointment>(AppointmentComparator);

  @Override
  public List<Appointment> listAllAppointments() {
    return listHelper(new DefaultPredicate());
  }

  @Override
  public List<Appointment> listUpcomingAppointments() {
    LocalDateTime now = LocalDateTime.now();
    return listHelper((new ElapsedPredicate(now)).notPredicate());
  }

  @Override
  public List<Appointment> listConflictingAppointments(Appointment appointment) {
    if (appointment == null) {
      throw new IllegalArgumentException(NULL_APPOINTMENT);
    }
    return listHelper(new ConflictPredicate(appointment));
  }

  @Override
  public boolean scheduleAppointment(Appointment appointment) {
    if (appointment == null) {
      throw new IllegalArgumentException(NULL_APPOINTMENT);
    }
    // Recall that comparator makes comparisons based on start time only.
    // Use floor function here to account for appointments scheduled at
    // specified appointment's start time.
    Appointment neighbor = schedule.floor(appointment);
    if (neighbor != null && appointment.doesConflict(neighbor)) {
      return false;
    }
    neighbor = schedule.higher(appointment);
    if (neighbor != null && appointment.doesConflict(neighbor)) {
      return false;
    }
    schedule.add(appointment);
    return true;
  }

  @Override
  public boolean cancelAppointment(Appointment appointment) {
    if (appointment == null) {
      throw new IllegalArgumentException(NULL_DATETIME);
    }
    // Need to grab appointment with (potentially) same start time,
    // and then check equality of appointments before removing.
    // This is again a consequence of comparator's behavior.
    Appointment other = schedule.floor(appointment);
    if (other != null && appointment.equals(other)) {
      return schedule.remove(appointment);
    }
    return false;
  }

  // Return a list of appointments filtered according to specified predicate.
  // See Predicate.java for more details.
  private List<Appointment> listHelper(Predicate predicate) {
    List<Appointment> list = new ArrayList<Appointment>();
    for (Appointment appointment : schedule) {
      if (predicate.accept(appointment)) {
        list.add(appointment);
      }
    }
    return list;
  }

}