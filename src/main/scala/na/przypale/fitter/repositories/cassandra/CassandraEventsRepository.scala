package na.przypale.fitter.repositories.cassandra

import java.util.Calendar

import com.datastax.driver.core.{Session, SimpleStatement}
import na.przypale.fitter.entities.Event
import na.przypale.fitter.repositories.{Dates, EventsRepository}

import scala.collection.JavaConverters

class CassandraEventsRepository(session: Session) extends EventsRepository {

  private lazy val createEventStatement = session.prepare(
    "INSERT INTO events(year, start_date, end_date, id, description, author, name, max_users_count) " +
      "VALUES(:year, :startDate, :endDate, :id, :description, :author, :name, :maxUsersCount)")
  override def create(event: Event): Unit = {
    val eventStartCalendar = Calendar.getInstance()
    eventStartCalendar.setTime(event.startDate)

    val createEventQuery = createEventStatement.bind()
      .setInt("year", eventStartCalendar.get(Calendar.YEAR))
      .setTimestamp("startDate", event.startDate)
      .setTimestamp("endDate", event.endDate)
      .setUUID("id", event.id)
      .setString("description", event.description)
      .setString("author", event.author)
      .setString("name", event.name)
      .setInt("maxUsersCount", event.maxParticipantsCount)

    session.execute(createEventQuery)
  }

  private lazy val findIncomingStatement = session.prepare(
    "SELECT year, start_date, end_date, id, description, author, name, max_users_count " +
    "FROM events" +
    "WHERE year IN :years AND start_date > now()")
  def findIncoming() {}

  private lazy val findEventsYearsStatement = new SimpleStatement("SELECT year FROM events")
  def findEventsYears(): Iterable[Int] = {
    val currentYear = Dates.currentYear()
    JavaConverters.collectionAsScalaIterable(session.execute(findEventsYearsStatement).all())
      .map(row => row.getInt("year"))
      .filter(year => year >= currentYear)
  }
}
