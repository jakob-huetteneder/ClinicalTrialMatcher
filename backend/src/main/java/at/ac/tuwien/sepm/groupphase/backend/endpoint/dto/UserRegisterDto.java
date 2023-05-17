package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class UserRegisterDto {

    @NotNull(message = "Email must not be null")
    @Email
    private String email;

    @NotNull(message = "Password must not be null")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRegisterDto userLoginDto)) {
            return false;
        }
        return Objects.equals(email, userLoginDto.email)
            && Objects.equals(password, userLoginDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    @Override
    public String toString() {
        return "UserLoginDto{"
            + "email='" + email + '\''
            + ", password='" + password + '\''
            + '}';
    }


    public static final class UserRegisterDtoBuilder {
        private String email;
        private String password;

        private UserRegisterDtoBuilder() {
        }

        public static UserRegisterDtoBuilder anUserLoginDto() {
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

        public UserRegisterDto build() {
            UserRegisterDto userLoginDto = new UserRegisterDto();
            userLoginDto.setEmail(email);
            userLoginDto.setPassword(password);
            return userLoginDto;
        }
    }
}
