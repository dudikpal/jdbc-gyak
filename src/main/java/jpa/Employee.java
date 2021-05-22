package jpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "employees")
//@IdClass(EmployeeId.class)
public class Employee {

    public enum EmployeeType {FULL_TIME, HALF_TIME}

    /*@Id
    private String depName;*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   /* @GeneratedValue(generator = "Emp_Gen")
    @TableGenerator(name = "Emp_Gen",
            table = "emp_id_gen",
            pkColumnName = "gen_name",
            valueColumnName = "gen_val")*/
    private Long id;

    /*@Embedded
    private EmployeeId employeeId;*/

    @Column(name = "emp_name")
    private String name;

    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType = EmployeeType.FULL_TIME;

    private LocalDate dateOfBirth;

    @ElementCollection
    @CollectionTable(name = "nicknames", joinColumns = @JoinColumn(name = "emp_id"))
    @Column(name = "nickname")
    private Set<String> nickNames;

    @ElementCollection
    @CollectionTable(name = "bookings", joinColumns = @JoinColumn(name = "emp_id"))
    @AttributeOverride(name = "startDate", column = @Column(name = "start_date"))
    @AttributeOverride(name = "daysTaken", column = @Column(name = "days"))
    private Set<VacationEntry> vacationBookings;

    /*@ElementCollection
    @CollectionTable(name = "phone_numbers", joinColumns = @JoinColumn(name = "emp_id"))
    @MapKeyColumn(name = "phone_type")
    @Column(name = "phone_number")
    private Map<String, String> phoneNumbers;*/

    @OneToOne
    private ParkingPlace parkingPlace;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "employee")
    //@OrderBy("type")
    @OrderColumn(name = "pos")
    private List<PhoneNumber> phoneNumbers;




    /*@PostPersist
    public void debugPersist() {
        System.out.println(name + " " + id);
    }*/


    public Employee() {
    }

    public Employee(String name) {
        this.name = name;
    }

    public Employee(Long id, String name) {

        this.id = id;
        this.name = name;
    }

    public Employee(String name, EmployeeType employeeType, LocalDate dateOfBirth) {

        this.id = id;
        this.name = name;
        this.employeeType = employeeType;
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }
    /*public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }*/

    public void addPhoneNumber(PhoneNumber phoneNumber) {
        if (phoneNumbers == null) {
            phoneNumbers = new ArrayList<>();
        }
        phoneNumbers.add(phoneNumber);
        phoneNumber.setEmployee(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getNickNames() {
        return nickNames;
    }

    public void setNickNames(Set<String> nickNames) {
        this.nickNames = nickNames;
    }

    public Set<VacationEntry> getVacationBookings() {
        return vacationBookings;
    }

    public void setVacationBookings(Set<VacationEntry> vacationBookings) {
        this.vacationBookings = vacationBookings;
    }

    /*public Map<String, String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Map<String, String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }*/

    public ParkingPlace getParkingPlace() {
        return parkingPlace;
    }

    public void setParkingPlace(ParkingPlace parkingPlace) {
        this.parkingPlace = parkingPlace;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
