### AppointmentSched

The user of this program is a hairdresser who needs to keep track of his or her appointments. There are two main appointment types: haircut (30 minutes) and shampoo (1 hour).  

Build:

`javac -d bin src/*.java`

Run:

`java -cp bin com.appointmentsched.demo.Demo`

Valid commands include `LIST`, `SCHEDULE type date time`, `CANCEL type date time`, and `EXIT`. Appointment types include `HAIRCUT` and `SHAMPOO`. Dates and times must adhere to the format `yyyy-M-d h:m a`. All commands/fields are case-insensitve. Some things to note:  

- References to `Appointment` instances are maintained using a tree data structure. This tree makes use of a `Comparator` that compares `Appointment`s for insertion based on start times. Scheduling conflicts are then determined based on `Appointment`s start times and durations.

- Flexibility was a major concern, so my program is meant to work with appointments of any duration, at any time. New appointment types can be created by simply adding a new `AppointmentType` enum, also located in the scheduler interface.

- In an effort to increase reusability and reduce code bloat, I created the abstract `Predicate` class. Developers can extend `Predicate` by providing an implementation for the abstract `accept` method, and then passing the predicate to the `listHelper` method which performs core iterate-and-accept functionalities. `Predicate`s can also be chained according to logical `and`/`or`/`not` operators: a simple example can be found in my `listUpcomingAppointments` implementation.

- Date parsing/formatting functionalities used throughout the `com.appointmentsched.scheduler` package can be found in the `DateUtils` class.

- Appointment scheduling and cancellation methods run in logarithmic `(O(lg(n)))` time. Listing methods run in `O(n)` time.

- My program makes use of the `java.time` library, and therefore requires Java 8.