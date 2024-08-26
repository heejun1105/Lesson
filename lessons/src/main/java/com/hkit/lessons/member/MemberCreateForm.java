package com.hkit.lessons.member;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreateForm {

	
    private String memberId;

   
    private String password1 = "";
    

    private String password2 = "";
    
    private LocalDate birth;
    //Date에서 년-월-일 표시를 위해 localDate로 수정
    
    private String phone;
    
    private String memberName;
    

    private String gender;

  
    private String email;
 
    private String address;
    
    private Long ProNum;

}
