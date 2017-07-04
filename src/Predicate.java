package com.appointmentsched.scheduler;

import com.appointmentsched.scheduler.Scheduler.Appointment;

/**
 * Abstract class Predicate acts as a template for appointment filters.
 * Also provides chaining functionalities, allowing developers to chain
 * an arbitrary number of predicates.
 *
 * @author Jeffrey Bour
 */
abstract class Predicate {

  /**
   * Abstract method to be defined.
   * @param appointment to check.
   * @return true if appointment is to be accepted, otherwise false.
   */
  abstract boolean accept(Appointment appointment);

  /**
   * Chains this predicate with another according to the logical 'and'
   * operator.
   * @param other predicate to be chained.
   * @return Predicate denoting the result of this.accept and other.accept.
   */
  Predicate andPredicate(Predicate other) {
    Predicate thisPredicate = this;
    return new Predicate() {
      boolean accept(Appointment appointment) {
        return thisPredicate.accept(appointment) && other.accept(appointment);
      }
    };
  }

  /**
   * Chains this predicate with another according to the logical 'or'
   * operator.
   * @param other predicate to be chained.
   * @return Predicate denoting the result of this.accept or other.accept.
   */
  Predicate orPredicate(Predicate other) {
    Predicate thisPredicate = this;
    return new Predicate() {
      boolean accept(Appointment appointment) {
        return thisPredicate.accept(appointment) || other.accept(appointment);
      }
    };
  }

  /**
   * Denoted the logical negation of this predicate.
   * @return Predicate denoting the result of not this.accept.
   */
  Predicate notPredicate() {
    Predicate thisPredicate = this;
    return new Predicate() {
      boolean accept(Appointment appointment) {
        return !thisPredicate.accept(appointment);
      }
    };
  }

}