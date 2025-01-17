package server.domain;

import javax.persistence.*;
import java.io.*;
import java.util.*;

//remove transiet
@Entity
@Table(name = "user_table")
public class User {
    @Transient
    private static final String ID_FILE_PATH = "lastAssignedId.txt";
    @Transient
    private static int lastAssignedId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private int userId;

    private String name;
    private String role;

    @Column(name = "username", unique = true)
    private String username;

    private String password;

    //database column name should be snake case
    //account_locked
    //^
    @Column(name = "accountLocked")
    private boolean accountLocked;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Slot> slots = new HashSet<>();


    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointmentV1List = new ArrayList<>();


    //remove static block
    static {
        loadLastAssignedId();
    }

    public User() {
    }

    //remove all following constructor
    public User(String name, String roll) {
        this(name, roll, "", "");
    }

    public User(String name, String role, String username, String password) {
        this.userId = generateNewId();
        this.name = name;
        this.role = role;
        this.username = username;
        this.password = password;
        this.accountLocked = false;
    }
    public User(int id, String name, String role, String username, String password, boolean accountLocked) {
        this.userId = id;
        this.name = name;
        this.role = role;
        this.username = username;
        this.password = password;
        this.accountLocked = accountLocked;
    }



    public User(int userId, String name, String role, String username, String password, boolean accountLocked, Set<Slot> slots, List<Appointment> appointmentV1List) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.username = username;
        this.password = password;
        this.accountLocked = accountLocked;
        this.slots = slots;
        this.appointmentV1List = appointmentV1List;
    }

    private static int generateNewId() {
        lastAssignedId++;
        saveLastAssignedId();
        return lastAssignedId;
    }

    private static void saveLastAssignedId() {
        try (FileWriter writer = new FileWriter(ID_FILE_PATH)) {
            writer.write(String.valueOf(lastAssignedId));
        } catch (IOException e) {
            System.out.println("Error saving lastAssignedId to file: " + e.getMessage());
        }
    }

    private static void loadLastAssignedId() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ID_FILE_PATH))) {
            String line = reader.readLine();
            if (line != null) {
                lastAssignedId = Integer.parseInt(line.trim());
            }
        } catch (FileNotFoundException e) {
            // File does not exist yet, initialize lastAssignedId to 0
            lastAssignedId = 0;
        } catch (IOException e) {
            System.out.println("Error loading lastAssignedId from file: " + e.getMessage());
        }
    }

    public void setUserId(int userId){
        this.userId = userId;
    }
    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountStatus(boolean status){
        this.accountLocked = status;
    }
    public boolean getAccountStatus(){
        return accountLocked;
    }


    public Set<Slot> getSlots() {
        return slots;
    }



    public List<Appointment> getAppointmentV1List(){
        return appointmentV1List;
    }


    public void addSlot(Slot slot) {
        slots.add(slot);
        slot.setDoctor(this);
    }

    public void removeSlot(Slot slot) {
        slots.remove(slot);
        slot.setDoctor(null);
    }

    public void addAppointmentV1(Appointment appointmentV1) {
        appointmentV1List.add(appointmentV1);
        appointmentV1.setPatient(this);
    }

    public void removeAppointment(Appointment appointmentV1){
        appointmentV1List.remove(appointmentV1);
        appointmentV1.setPatient(null);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accountStatus='" + accountLocked + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId &&
                accountLocked == user.accountLocked &&
                Objects.equals(name, user.name) &&
                Objects.equals(role, user.role) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, role, username, password, accountLocked);
    }
}
