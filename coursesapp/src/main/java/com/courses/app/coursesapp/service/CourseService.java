package com.courses.app.coursesapp.service;

import com.courses.app.coursesapp.entity.CourseEntity;
import com.courses.app.coursesapp.response.CourseResponse;

import java.util.List;

public interface CourseService {
    CourseEntity getcoursesbyId(long id);

    List<CourseEntity> getallcourses();

    void postcourses(long student_id, CourseResponse courseResponse);

    List<CourseEntity> getcoursesbystudentId(long studentid);

    void putcourses(Long id, CourseResponse courseResponse);

    void deleteCourses(Long student_id);
}
