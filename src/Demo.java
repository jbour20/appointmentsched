package com.appointmentsched.demo;

import com.appointmentsched.scheduler.DateUtils;
import com.appointmentsched.scheduler.Schedule;
import com.appointmentsched.scheduler.Scheduler;
import com.appointmentsched.scheduler.Scheduler.Appointment;
import com.appointmentsched.scheduler.Scheduler.AppointmentType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.time.format.DateTimeParseException;
import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.List;

/**
 * Class Demo serves as a simple CLI for the com.appointmentsched.scheduler
 * package.
 *
 * @author Jeffrey Bour
 */
public class Demo {

  private final Scheduler schedule = new Schedule();


  /**
   * Simple enum consisting of supported commands and corresponding
   * usage messages.
   */
  private enum Command {

    LIST("Usage: LIST"),
    SCHEDULE("Usage: SCHEDULE type date time"),
    CANCEL("Usage: CANCEL type date time"),
    EXIT("Usage: EXIT");

    private final String usage;

    Command(String usage) {
      this.usage = usage;
    }

    String getUsage() {
      return usage;
    }

  }

  /**
   * Simple enum consisting of default messages for Scheduler command
   * results and potential errors.
   */
  private enum DefaultMessages {

    CANCEL_ERROR("Could not cancel appointment"),
    EMPTY_SCHEDULE("No appointments scheduled"),
    IO_ERROR("There was an error processing your request"),
    PARSE_DATE_ERROR("Unable to parse date"),
    SCHEDULE_CONFLICT("Schedule conflict"),
    UNRECOGNIZED_APPOINTMENT("Unrecognized appointment"),
    UNRECOGNIZED_COMMAND("Unrecognized command");

    private final String message;

    DefaultMessages(String message) {
      this.message = message;
    }

    String getMessage() {
      return message;
    }

  }

  public static void main(String[] args) {
    Demo demo = new Demo();
    demo.go();
  }

  /**
   * Start the program. Open a BufferedReader and wait for commands issued via
   * standard input.
   */
  private void go() {
    BufferedReader reader =
        new BufferedReader(new InputStreamReader(System.in));
    String input = "";
    boolean haveNext = true;
    while (haveNext) {
      try {
        input = reader.readLine();
        haveNext = parseAndExecute(input);
      } catch (IOException e) {
        handleOutput(DefaultMessages.IO_ERROR.getMessage());
      }
    }
    try {
      reader.close();
    } catch (IOException e) {
      // Shutting down, ignore exception
    }
  }

  /**
   * Determine which command to execute. Input, valid or otherwise, is
   * handled accordingly.
   */
  private boolean parseAndExecute(String input) {
    String[] tokens = input.toUpperCase().split("\\s+");
    String message = null;
    boolean haveNext = true;
    if (tokens.length > 0 && !tokens[0].equals("")) {
      try {
        Command command = Command.valueOf(tokens[0]);
        switch (command) {
          case LIST:
            message = doList(tokens);
            break;
          case SCHEDULE:
            message = doSchedule(tokens);
            break;
          case CANCEL:
            message = doCancel(tokens);
            break;
          case EXIT:
            message = doExit(tokens);
            if (message == null) {
              haveNext = false;
            }
            break;
        }
      } catch (IllegalArgumentException e) {
        message = DefaultMessages.UNRECOGNIZED_COMMAND.getMessage();
      }
    }
    handleOutput(message);
    return haveNext;
  }

  /**
   * The following methods all perform simple parsing for their respective
   * scheduler commands. If parsing is successful, the command is executed
   * and a message (if necessary) is returned.
   */
  private String doList(String[] tokens) {
    if (tokens.length != 1) {
      return Command.LIST.getUsage();
    }
    List<Appointment> appointments = schedule.listUpcomingAppointments();
    if (appointments.isEmpty()) {
      return DefaultMessages.EMPTY_SCHEDULE.getMessage();
    }
    return stringifyList(appointments);
  }

  private String doSchedule(String[] tokens) {
    if (tokens.length < 3) {
      return Command.SCHEDULE.getUsage();
    }
    AppointmentType type = parseAppointmentType(tokens[1]);
    if (type == null) {
      return DefaultMessages.UNRECOGNIZED_APPOINTMENT.getMessage();
    }
    LocalDateTime dateTime = parseDateTime(
        String.join(" ", Arrays.copyOfRange(tokens, 2, tokens.length)));
    if (dateTime == null) {
      return DefaultMessages.PARSE_DATE_ERROR.getMessage();
    }
    Appointment appointment = new Appointment(type, dateTime);
    if (!schedule.scheduleAppointment(appointment)) {
      List<Appointment> conflicts =
          schedule.listConflictingAppointments(appointment);
      return DefaultMessages.SCHEDULE_CONFLICT.getMessage()
          + "\n" + stringifyList(conflicts);
    }
    return null;
  }

  private String doCancel(String[] tokens) {
    if (tokens.length < 3) {
      return Command.CANCEL.getUsage();
    }
    AppointmentType type = parseAppointmentType(tokens[1]);
    if (type == null) {
      return DefaultMessages.UNRECOGNIZED_APPOINTMENT.getMessage();
    }
    LocalDateTime dateTime = parseDateTime(
        String.join(" ", Arrays.copyOfRange(tokens, 2, tokens.length)));
    if (dateTime == null) {
      return DefaultMessages.PARSE_DATE_ERROR.getMessage();
    }
    Appointment appointment = new Appointment(type, dateTime);
    if (!schedule.cancelAppointment(appointment)) {
      return DefaultMessages.CANCEL_ERROR.getMessage();
    }
    return null;
  }

  private String doExit(String[] tokens) {
    if (tokens.length != 1) {
      return Command.EXIT.getUsage();
    }
    return null;
  }

  /**
   * Helper method to parse appointment type. Return null if unable to parse.
   */
  private AppointmentType parseAppointmentType(String description) {
    AppointmentType type = null;
    try {
      type = AppointmentType.valueOf(description);
    } catch (IllegalArgumentException e) {
      // Ignore, return null
    }
    return type;
  }

  /**
   * Helper method to parse date/time. Return null if unable to parse.
   */
  private LocalDateTime parseDateTime(String description) {
    LocalDateTime dateTime = null;
    try {
      dateTime = DateUtils.parse(description);
    } catch (DateTimeParseException e) {
      // Ignore, return null
    }
    return dateTime;
  }

  /**
   * Helper method to create a nicely-formatted string from a
   * list of appointments.
   */
  private String stringifyList(List<Appointment> appointments) {
    StringBuffer buffer = new StringBuffer();
    boolean haveFirst = false;
    for (Appointment appointment : appointments) {
      buffer.append(haveFirst ? "\n" : "");
      buffer.append(appointment.toString());
      haveFirst = true;
    }
    return buffer.toString();
  }

  /**
   * Print responses to standard output.
   */
  private void handleOutput(String message) {
    if (message != null) {
      System.out.println(message);
    }
  }

}