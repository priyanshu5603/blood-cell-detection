package com.scm.majProj.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class userForm {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phoneNumber;
    //information
    private boolean emailVerified = false;
    private boolean phoneVerified = false;

}
