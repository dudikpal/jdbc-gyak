package jpa;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class VacationEntry {

    private LocalDate startDate;

    private int daysTaken;

    public VacationEntry() {
    }

    public VacationEntry(LocalDate startDate, int daysTaken) {
        this.startDate = startDate;
        this.daysTaken = daysTaken;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getDaysTaken() {
        return daysTaken;
    }

    public void setDaysTaken(int daysTaken) {
        this.daysTaken = daysTaken;
    }
}
