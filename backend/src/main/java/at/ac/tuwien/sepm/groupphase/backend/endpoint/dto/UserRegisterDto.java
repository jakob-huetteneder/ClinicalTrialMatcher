package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;

public class UserRegisterDto {

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    private String email;
    @NotEmpty(message = "Password must not be empty")
    private String password;
    @NotEmpty(message = "First name must not be empty")
    private String firstName;
    @NotEmpty(message = "Last name must not be empty")
    private String lastName;
    @NotNull(message = "User role must be selected")
    private Role role;
    private LocalDate birthdate;
    private Gender gender;
    private boolean admin;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isCreatedByAdmin() {
        return admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRegisterDto that = (UserRegisterDto) o;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password)
            && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, firstName, lastName, role);
    }

    @Override
    public String toString() {
        return "UserRegisterDto{"
            + "email='" + email + '\''
            + ", password='" + password + '\''
            + ", firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\''
            + ", role='" + role + '\''
            + '}';
    }


    public static final class UserRegisterDtoBuilder {
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private Role role;
        private Gender gender;
        private LocalDate birthdate;
        private boolean admin;

        private UserRegisterDtoBuilder() {
        }

        public static UserRegisterDtoBuilder anUserRegisterDto() {
            return new UserRegisterDtoBuilder();
        }

        public UserRegisterDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserRegisterDtoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserRegisterDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserRegisterDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserRegisterDtoBuilder withRole(Role role) {
            this.role = role;
            return this;
        }

        public UserRegisterDtoBuilder withDob(LocalDate dob) {
            birthdate = dob;
            return this;
        }

        public UserRegisterDtoBuilder withGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public UserRegisterDtoBuilder withGender(Boolean admin) {
            this.admin = admin;
            return this;
        }

        public UserRegisterDto build() {
            UserRegisterDto userRegisterDto = new UserRegisterDto();
            userRegisterDto.setEmail(email);
            userRegisterDto.setPassword(password);
            userRegisterDto.setFirstName(firstName);
            userRegisterDto.setLastName(lastName);
            userRegisterDto.setRole(role);
            userRegisterDto.setGender(gender);
            userRegisterDto.setBirthdate(birthdate);
            userRegisterDto.setAdmin(admin);
            return userRegisterDto;
        }
    }
}
