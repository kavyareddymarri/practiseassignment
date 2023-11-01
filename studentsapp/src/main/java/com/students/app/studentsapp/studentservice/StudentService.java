package com.students.app.studentsapp.studentservice;

import com.students.app.studentsapp.studentresponse.StudentResponse;

import java.util.List;

public interface StudentService {
    StudentResponse getstudentsbyid(long id);

    List<StudentResponse> getallstudents();

    void poststudents(StudentResponse studentResponse);

    void updatestudents(Long id, StudentResponse studentResponse);

    void deletestudents(Long id);
}
